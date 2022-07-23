package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcherImpl
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.Result
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.usecase.*
import dmitry.molchanov.fishingforecast.utils.ONE_DAY
import dmitry.molchanov.fishingforecast.utils.TimeMs
import dmitry.molchanov.fishingforecast.utils.nightTime
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ResultViewModel(
    getResultUseCase: GetResultsUseCase,
    commonProfileFetcher: Lazy<CommonProfileFetcherImpl>,
    private val saveResultUseCase: Lazy<SaveResultUseCase>,
    private val getProfilesUseCase: Lazy<GetProfilesUseCase>,
    private val getMapPointsUseCase: Lazy<GetMapPointsUseCase>,
    private val getSavedWeatherDataUseCase: Lazy<GetSavedWeatherDataUseCase>,
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<ResultEvent>(replay = 1)
    val messageFlow = _messageFlow.asSharedFlow()

    private val _stateFlow = MutableStateFlow(ResultScreenState(selectedProfile = commonProfileFetcher.value.instance))
    val stateFlow = _stateFlow.asStateFlow()

    init {
        observeResults(getResultUseCase)
        updateDates()
        updateProfiles()
        updateMapPoints()
    }

    private fun observeResults(getResultUseCase: GetResultsUseCase) {
        getResultUseCase.executeFlow()
            .onEach { results ->
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
        is CreateResult -> tryCreateResult()
        is AddResultClickAction -> showAddDialog()
        is CloseAddResultDialog -> closeAddResultDialog()
        is DateSelected -> updateSelectedDate(action.date)
        is ProfileSelected -> updateSelectedProfile(action.profile)
        is MapPointSelected -> updateSelectedMapPoint(action.mapPoint)
        is ChangeDialogStatus -> updateDialogStatus(action.isVisible)
    }

    private fun updateDialogStatus(isVisible: Boolean) {
        _stateFlow.update { it.copy(shouldShowDialog = isVisible) }
    }

    private fun tryCreateResult() {
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

sealed class ResultAction
class AddResultClickAction : ResultAction()
class CloseAddResultDialog : ResultAction()
class DateSelected(val date: Long) : ResultAction()
class ProfileSelected(val profile: Profile) : ResultAction()
class MapPointSelected(val mapPoint: MapPoint) : ResultAction()
class CreateResult() : ResultAction()
class ChangeDialogStatus(val isVisible: Boolean) : ResultAction()