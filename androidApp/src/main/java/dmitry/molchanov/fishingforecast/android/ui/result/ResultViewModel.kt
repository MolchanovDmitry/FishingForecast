package dmitry.molchanov.fishingforecast.android.ui.result

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.mapper.deserialize
import dmitry.molchanov.domain.mapper.string
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.utils.ONE_DAY
import dmitry.molchanov.domain.utils.TimeMs
import dmitry.molchanov.domain.utils.nightTime
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcherImpl
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.Result
import dmitry.molchanov.fishingforecast.model.SharedResult
import dmitry.molchanov.fishingforecast.usecase.GetMapPointsUseCase
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import dmitry.molchanov.fishingforecast.usecase.GetResultsUseCase
import dmitry.molchanov.fishingforecast.usecase.GetSavedWeatherDataUseCase
import dmitry.molchanov.fishingforecast.usecase.GetWeatherDataByResultUseCase
import dmitry.molchanov.fishingforecast.usecase.ImportSharedResultUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveResultUseCase
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ResultViewModel(
    getResultUseCase: GetResultsUseCase,
    commonProfileFetcher: Lazy<CommonProfileFetcherImpl>,
    private val context: Context, // TODO удалить текущую затычку.
    private val saveResultUseCase: Lazy<SaveResultUseCase>,
    private val getProfilesUseCase: Lazy<GetProfilesUseCase>,
    private val getMapPointsUseCase: Lazy<GetMapPointsUseCase>,
    private val importSharedResultUseCase: Lazy<ImportSharedResultUseCase>,
    private val getSavedWeatherDataUseCase: Lazy<GetSavedWeatherDataUseCase>,
    private val getWeatherDataByResultUseCase: Lazy<GetWeatherDataByResultUseCase>,
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<ResultEvent>(replay = 1)
    val messageFlow = _messageFlow.asSharedFlow()

    private val _stateFlow = MutableStateFlow(ResultScreenState(selectedProfile = commonProfileFetcher.value.instance))
    val stateFlow = _stateFlow.asStateFlow()

    private var results: List<Result>? = null

    init {
        observeResults(getResultUseCase)
        updateDates()
        updateProfiles()
        updateMapPoints()
    }

    private fun observeResults(getResultUseCase: GetResultsUseCase) {
        getResultUseCase.executeFlow()
            .onEach { results ->
                this.results = results
                _stateFlow.update { it.copy(results = results) }
            }
            .launchIn(viewModelScope)
    }

    private fun updateDates() {
        val currentDate: TimeMs = System.currentTimeMillis()
        val nightTime = currentDate.nightTime
        // TODO динамически добавлять из настроек
        _stateFlow.update {
            it.copy(
                dates = listOf(
                    nightTime,
                    nightTime - ONE_DAY,
                    nightTime - ONE_DAY - ONE_DAY,
                    nightTime - ONE_DAY - ONE_DAY - ONE_DAY,
                    nightTime - ONE_DAY - ONE_DAY - ONE_DAY - ONE_DAY,
                )
            )
        }
    }

    fun onAction(action: ResultAction) = when (action) {
        is AddResultClickAction -> showAddDialog()
        is CloseAddResultDialog -> closeAddResultDialog()
        is DateSelected -> updateSelectedDate(action.date)
        is CreateResult -> tryCreateResult(action.resultName)
        is ProfileSelected -> updateSelectedProfile(action.profile)
        is MapPointSelected -> updateSelectedMapPoint(action.mapPoint)
        is ChangeDialogStatus -> updateDialogStatus(action.isVisible)
        is SaveToStorageAndShareClick -> onShareClick()
    }

    // TODO сделать сериализацию через input stream
    fun importResult(inputStream: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = convertStreamToString(inputStream) ?: TODO()
            val sharedResults = result.deserialize<List<SharedResult>>()
            importSharedResultUseCase.value.execute(sharedResults)
        }
    }

    @Throws(java.lang.Exception::class)
    private fun convertStreamToString(inputStream: InputStream?): String? {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }

    // TODO реализовать публикацию только выбранных результатов
    private fun onShareClick() {
        viewModelScope.launch(ioDispatcher) {
            results
                ?.map { result ->
                    val weatherData = getWeatherDataByResultUseCase.value.execute(result)
                    SharedResult(result, weatherData)
                }?.string()
                ?.let { resultJson ->
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                    val fileNamePrefix = dateFormat.format(System.currentTimeMillis())
                    val fileName = "$fileNamePrefix.txt"

                    val file = File(context.cacheDir, fileName)
                    file.writeText(String(resultJson.toByteArray(), charset("UTF-8")))

                    _messageFlow.tryEmit(ShareFile(file.path))

                    //try {

                    writeToFile(fileName = fileName, data = resultJson)
                    /*} catch (t: Throwable) {
                        // ignore
                    }*/
                }
        }
    }

    //TODO актуализировать
    private fun writeToFile(fileName: String, data: String) {
        val env = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val stream = FileOutputStream("$env/$fileName")
        stream.write(data.toByteArray())
    }

    private fun updateDialogStatus(isVisible: Boolean) {
        _stateFlow.update { it.copy(shouldShowDialog = isVisible) }
    }

    private fun tryCreateResult(resultName: String) {
        val selectedMapPoint = _stateFlow.value.selectedMapPoint ?: run {
            _messageFlow.tryEmit(NullMapPoint())
            return
        }
        val date =
            System.currentTimeMillis()//_stateFlow.value.selectedDate // TODO выбранная дата минус заданое количество
        viewModelScope.launch {
            val weatherData: List<WeatherData> =
                getSavedWeatherDataUseCase.value
                    .execute(selectedMapPoint, from = date - (5 * ONE_DAY), to = date + ONE_DAY - 1)
            saveResultUseCase.value.execute(
                resultName = resultName,
                weatherData = weatherData,
                mapPoint = selectedMapPoint,
                profile = stateFlow.value.selectedProfile,
            )
        }

    }

    private fun updateSelectedDate(date: TimeMs) {
        _stateFlow.update { it.copy(selectedDate = date) }
    }

    private fun updateSelectedProfile(profile: Profile) {
        _stateFlow.update { it.copy(selectedProfile = profile) }
    }

    private fun updateSelectedMapPoint(mapPoint: MapPoint) {
        _stateFlow.update { it.copy(selectedMapPoint = mapPoint) }
    }

    private fun updateMapPoints() {
        viewModelScope.launch {
            val mapPoints = getMapPointsUseCase.value.execute()
            _stateFlow.update { it.copy(mapPoints = mapPoints, selectedMapPoint = mapPoints.firstOrNull()) }

        }
    }

    private fun updateProfiles() {
        viewModelScope.launch {
            val profiles = getProfilesUseCase.value.execute()
            _stateFlow.update { it.copy(profiles = profiles) }
        }
    }

    private fun closeAddResultDialog() {
        _stateFlow.update { it.copy(shouldShowDialog = false) }
    }

    private fun showAddDialog() {
        _stateFlow.update { it.copy(shouldShowDialog = true) }
    }

}

data class ResultScreenState(
    val selectedDate: Long = 0,
    val selectedProfile: Profile,
    val selectedMapPoint: MapPoint? = null,
    val dates: List<TimeMs> = emptyList(),
    val shouldShowDialog: Boolean = false,
    val profiles: List<Profile> = emptyList(),
    val mapPoints: List<MapPoint> = emptyList(),
    val results: List<Result> = emptyList(),
)

sealed class ResultEvent
class NullMapPoint : ResultEvent()
class ShareFile(val filePath: String) : ResultEvent()

sealed class ResultAction
class AddResultClickAction : ResultAction()
class CloseAddResultDialog : ResultAction()
class DateSelected(val date: Long) : ResultAction()
class ProfileSelected(val profile: Profile) : ResultAction()
class MapPointSelected(val mapPoint: MapPoint) : ResultAction()
class CreateResult(val resultName: String) : ResultAction()
class ChangeDialogStatus(val isVisible: Boolean) : ResultAction()
class SaveToStorageAndShareClick : ResultAction()