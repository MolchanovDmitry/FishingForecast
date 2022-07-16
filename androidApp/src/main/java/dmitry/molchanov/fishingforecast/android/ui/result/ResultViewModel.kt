package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcher
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.GetMapPointsUseCase
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import dmitry.molchanov.fishingforecast.usecase.GetSavedWeatherDataUseCase
import dmitry.molchanov.fishingforecast.utils.ONE_DAY
import dmitry.molchanov.fishingforecast.utils.TimeMs
import dmitry.molchanov.fishingforecast.utils.nightTime
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ResultViewModel(
    private val getProfilesUseCase: GetProfilesUseCase,
    private val getMapPointsUseCase: GetMapPointsUseCase,
    private val commonProfileFetcher: CommonProfileFetcher,
    private val getSavedWeatherDataUseCase: GetSavedWeatherDataUseCase
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<ResultEvent>(replay = 1)
    val messageFlow = _messageFlow.asSharedFlow()

    private val _stateFlow = MutableStateFlow(ResultScreenState(selectedProfile = commonProfileFetcher.get()))
    val stateFlow = _stateFlow.asStateFlow()

    init {
        updateDates()
        updateProfiles()
        updateMapPoints()
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
        val selectedMap = _stateFlow.value.selectedMapPoint ?: run {
            _messageFlow.tryEmit(NullMapPoint())
            return
        }
    }

    private fun updateSelectedDate(date: TimeMs) {
        _stateFlow.update { it.copy(selectedDate = date) }
        _stateFlow.value.selectedMapPoint?.let { mapPoint ->
            viewModelScope.launch {
                val weatherData = getSavedWeatherDataUseCase.execute(mapPoint, date, date + ONE_DAY - 1)
            }
        }
    }

    private fun updateSelectedProfile(profile: Profile) {
        _stateFlow.update { it.copy(selectedProfile = profile) }
    }

    private fun updateSelectedMapPoint(mapPoint: MapPoint) {
        _stateFlow.update { it.copy(selectedMapPoint = mapPoint) }
    }

    private fun updateMapPoints() {
        viewModelScope.launch {
            val mapPoints = getMapPointsUseCase.execute()
            _stateFlow.update { it.copy(mapPoints = mapPoints) }
        }
    }

    private fun updateProfiles() {
        viewModelScope.launch {
            val profiles = getProfilesUseCase.execute() + commonProfileFetcher.get()
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