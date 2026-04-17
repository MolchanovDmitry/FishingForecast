package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.mapper.deserialize
import dmitry.molchanov.domain.mapper.string
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.SharedResult
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.usecase.GetMapPointsUseCase
import dmitry.molchanov.domain.usecase.GetProfilesUseCase
import dmitry.molchanov.domain.usecase.GetResultsUseCase
import dmitry.molchanov.domain.usecase.GetSavedWeatherDataUseCase
import dmitry.molchanov.domain.usecase.GetWeatherDataByResultUseCase
import dmitry.molchanov.domain.usecase.ImportSharedResultUseCase
import dmitry.molchanov.domain.usecase.SaveResultUseCase
import dmitry.molchanov.domain.utils.ONE_DAY
import dmitry.molchanov.domain.utils.TimeMs
import dmitry.molchanov.domain.utils.nightTime
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcherImpl
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
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
    private val saveResultUseCase: Lazy<SaveResultUseCase>,
    private val getProfilesUseCase: Lazy<GetProfilesUseCase>,
    private val getMapPointsUseCase: Lazy<GetMapPointsUseCase>,
    private val importSharedResultUseCase: Lazy<ImportSharedResultUseCase>,
    private val getSavedWeatherDataUseCase: Lazy<GetSavedWeatherDataUseCase>,
    private val getWeatherDataByResultUseCase: Lazy<GetWeatherDataByResultUseCase>,
    private val resultDataRepository: dmitry.molchanov.domain.repository.ResultDataRepository
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<ResultEvent>(replay = 1)
    val messageFlow = _messageFlow.asSharedFlow()

    private val _stateFlow =
        MutableStateFlow(ResultScreenState(selectedProfile = commonProfileFetcher.value.instance))
    val stateFlow = _stateFlow.asStateFlow()

    private var results: List<Result>? = null

    init {
        updateDates()
        updateProfiles()
        updateMapPoints()
        observeResults()
    }

    private fun observeResults() {
        val flow = if (_stateFlow.value.sortByRating) {
            resultDataRepository.getResultsFlowOrderByRating()
        } else {
            resultDataRepository.getResultsFlowOrderByDate()
        }
        flow.onEach { results ->
            this.results = results
            val resultsWithDates = results.map { result ->
                val minDate = kotlin.runCatching {
                    resultDataRepository.getMinWeatherDateByResult(result.id)
                }.getOrNull()
                val maxDate = kotlin.runCatching {
                    resultDataRepository.getMaxWeatherDateByResult(result.id)
                }.getOrNull()
                ResultWithDates(result, minDate, maxDate)
            }
            _stateFlow.update { it.copy(resultsWithDates = resultsWithDates) }
        }.launchIn(viewModelScope)
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
                    nightTime - ONE_DAY - ONE_DAY - ONE_DAY - ONE_DAY
                )
            )
        }
    }

    fun onAction(action: ResultAction) = when (action) {
        is AddResultClickAction -> showAddDialog()
        is CloseAddResultDialog -> closeAddResultDialog()
        is DateSelected -> updateSelectedDate(action.date)
        is CreateResult -> tryCreateResult(action.resultName)
        is CreateResultWithDetails -> tryCreateResultWithDetails(action.resultName, action.rating, action.description)
        is ProfileSelected -> updateSelectedProfile(action.profile)
        is MapPointSelected -> updateSelectedMapPoint(action.mapPoint)
        is ChangeDialogStatus -> updateDialogStatus(action.isVisible)
        is SaveToStorageAndShareClick -> onShareClick()
        is EditResultClick -> showEditDialog(action.result)
        is CloseEditDialog -> closeEditDialog()
        is SaveEditedResult -> saveEditedResult(action.resultId, action.newName, action.newRating, action.newDescription)
        is SetRating -> setRating(action.resultId, action.rating)
        is ToggleSortOrder -> toggleSortOrder()
    }

    // TODO сделать сериализацию через input stream
    fun importResult(inputStream: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = convertStreamToString(inputStream) ?: TODO()
                val sharedResults = result.deserialize<List<SharedResult>>()
                importSharedResultUseCase.value.execute(sharedResults)
            } catch (e: Exception) {
                e.printStackTrace()
                _messageFlow.tryEmit(Error(e.message ?: "Ошибка импорта результата"))
            }
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
            try {
                val sharedResults = results?.map { result ->
                    val weatherData = getWeatherDataByResultUseCase.value.execute(result)
                    SharedResult(result, weatherData)
                } ?: return@launch

                val resultJson = sharedResults.string()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                val fileNamePrefix = dateFormat.format(System.currentTimeMillis())
                val fileName = "$fileNamePrefix.txt"

                _messageFlow.tryEmit(ShareResult(resultJson, fileName))
            } catch (e: Exception) {
                e.printStackTrace()
                _messageFlow.tryEmit(Error("Ошибка сохранения результата"))
            }
        }
    }

    private fun updateDialogStatus(isVisible: Boolean) {
        _stateFlow.update { it.copy(shouldShowDialog = isVisible) }
    }

    private fun tryCreateResult(resultName: String) {
        val selectedMapPoint = _stateFlow.value.selectedMapPoint ?: run {
            _messageFlow.tryEmit(Error("Выберите точку"))
            return
        }
        val date =
            System.currentTimeMillis() // _stateFlow.value.selectedDate // TODO выбранная дата минус заданое количество
        viewModelScope.launch {
            try {
                val weatherData: List<WeatherData> = getSavedWeatherDataUseCase.value.execute(
                    selectedMapPoint,
                    from = date - (5 * ONE_DAY),
                    to = date + ONE_DAY - 1
                )
                saveResultUseCase.value.execute(
                    resultName = resultName,
                    weatherData = weatherData,
                    mapPoint = selectedMapPoint,
                    profile = stateFlow.value.selectedProfile
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _messageFlow.tryEmit(Error("Ошибка создания результата"))
            }
        }
    }

    private fun tryCreateResultWithDetails(resultName: String, rating: Int, description: String) {
        val selectedMapPoint = _stateFlow.value.selectedMapPoint ?: run {
            _messageFlow.tryEmit(Error("Выберите точку"))
            return
        }
        val date = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                val weatherData: List<WeatherData> = getSavedWeatherDataUseCase.value.execute(
                    selectedMapPoint,
                    from = date - (5 * ONE_DAY),
                    to = date + ONE_DAY - 1
                )
                val actualRating = if (rating > 0) rating else null
                val actualDescription = if (description.isNotBlank()) description else null
                saveResultUseCase.value.execute(
                    resultName = resultName,
                    weatherData = weatherData,
                    mapPoint = selectedMapPoint,
                    profile = stateFlow.value.selectedProfile,
                    rating = actualRating,
                    description = actualDescription
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _messageFlow.tryEmit(Error("Ошибка создания результата"))
            }
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
            try {
                val mapPoints = getMapPointsUseCase.value.execute()
                _stateFlow.update {
                    it.copy(
                        mapPoints = mapPoints,
                        selectedMapPoint = mapPoints.firstOrNull()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _messageFlow.tryEmit(Error("Ошибка загрузки точек"))
            }
        }
    }

    private fun updateProfiles() {
        viewModelScope.launch {
            try {
                val profiles = getProfilesUseCase.value.execute()
                _stateFlow.update { it.copy(profiles = profiles) }
            } catch (e: Exception) {
                e.printStackTrace()
                _messageFlow.tryEmit(Error("Ошибка загрузки профилей"))
            }
        }
    }

    private fun closeAddResultDialog() {
        _stateFlow.update { it.copy(shouldShowDialog = false) }
    }

    private fun showAddDialog() {
        _stateFlow.update { it.copy(shouldShowDialog = true) }
    }

    companion object {
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        fun formatDate(timestamp: Long): String = dateFormat.format(timestamp)
    }

    private fun showEditDialog(result: Result) {
        _stateFlow.update { it.copy(shouldShowEditDialog = true, editingResult = result) }
    }

    private fun closeEditDialog() {
        _stateFlow.update { it.copy(shouldShowEditDialog = false, editingResult = null) }
    }

    private fun saveEditedResult(resultId: Long, newName: String, newRating: Int, newDescription: String) {
        if (newName.isBlank()) {
            _messageFlow.tryEmit(Error("Название не может быть пустым"))
            return
        }
        viewModelScope.launch {
            try {
                resultDataRepository.updateResultName(resultId, newName)
                if (newRating > 0) {
                    resultDataRepository.updateResultRating(resultId, newRating)
                }
                if (newDescription.isNotBlank()) {
                    resultDataRepository.updateResultDescription(resultId, newDescription)
                }
                closeEditDialog()
            } catch (e: Exception) {
                e.printStackTrace()
                _messageFlow.tryEmit(Error("Ошибка сохранения"))
            }
        }
    }

    private fun setRating(resultId: Long, rating: Int) {
        viewModelScope.launch {
            try {
                resultDataRepository.updateResultRating(resultId, rating)
            } catch (e: Exception) {
                e.printStackTrace()
                _messageFlow.tryEmit(Error("Ошибка сохранения рейтинга"))
            }
        }
    }

    private fun toggleSortOrder() {
        _stateFlow.update { it.copy(sortByRating = !it.sortByRating) }
        observeResults()
    }
}

data class ResultWithDates(
    val result: Result,
    val minDate: Long?,
    val maxDate: Long?
)

data class ResultScreenState(
    val selectedDate: Long = 0,
    val selectedProfile: Profile,
    val selectedMapPoint: MapPoint? = null,
    val dates: List<TimeMs> = emptyList(),
    val shouldShowDialog: Boolean = false,
    val shouldShowEditDialog: Boolean = false,
    val editingResult: Result? = null,
    val sortByRating: Boolean = false,
    val profiles: List<Profile> = emptyList(),
    val mapPoints: List<MapPoint> = emptyList(),
    val resultsWithDates: List<ResultWithDates> = emptyList()
)

sealed class ResultEvent
data class Error(val message: String) : ResultEvent()
data class ShareResult(val data: String, val fileName: String) : ResultEvent()

sealed class ResultAction
class AddResultClickAction : ResultAction()
class CloseAddResultDialog : ResultAction()
class DateSelected(val date: Long) : ResultAction()
class ProfileSelected(val profile: Profile) : ResultAction()
class MapPointSelected(val mapPoint: MapPoint) : ResultAction()
class CreateResult(val resultName: String) : ResultAction()
class CreateResultWithDetails(val resultName: String, val rating: Int, val description: String) : ResultAction()
class ChangeDialogStatus(val isVisible: Boolean) : ResultAction()
class SaveToStorageAndShareClick : ResultAction()
class EditResultClick(val result: Result) : ResultAction()
class CloseEditDialog : ResultAction()
class SaveEditedResult(val resultId: Long, val newName: String, val newRating: Int, val newDescription: String) : ResultAction()
class SetRating(val resultId: Long, val rating: Int) : ResultAction()
class ToggleSortOrder : ResultAction()
