package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    getProfilesUseCase: GetProfilesUseCase,
    getMapPointsUseCase: GetMapPointsUseCase,
    getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val saveMapPointUseCase: SaveMapPointUseCase,
    private val deleteProfileUseCase: Lazy<DeleteProfileUseCase>,
    private val selectProfileUseCase: Lazy<SelectProfileUseCase>,
    private val saveForecastSettingMarkUseCase: Lazy<SaveForecastSettingMarkUseCase>,
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    private var allMapPoints: List<MapPoint> = emptyList()

    init {

        getCurrentProfileUseCase.execute()
            .onEach { profile ->
                _state.update { mainViewState ->
                    mainViewState.copy(
                        currentProfile = profile,
                        mapPoints = if (profile.isCommon) {
                            allMapPoints
                        } else {
                            allMapPoints.filter { it.profileName == profile.name }
                        }
                    )
                }
            }
            .launchIn(viewModelScope)

        getProfilesUseCase.execute()
            .onEach { profiles ->
                _state.update { it.copy(profiles = profiles) }
            }.launchIn(viewModelScope)

        getMapPointsUseCase.execute()
            .onEach { mapPoints ->
                this.allMapPoints = mapPoints
                _state.update { mainViewState ->
                    mainViewState.copy(mapPoints =
                    if (state.value.currentProfile.isCommon) {
                        mapPoints
                    } else {
                        mapPoints.filter { it.profileName == state.value.currentProfile.name }
                    })
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is SavePoint -> saveMapPoint(event)
            is CreateProfile -> createProfile(event.name)
            is DeleteProfile -> deleteProfile(event.name)
            is SelectProfile -> selectProfile(event.name)
            is SaveForecastSettingMark -> saveForecastSettingMark(event)
        }
    }

    private fun saveForecastSettingMark(event: SaveForecastSettingMark) {
        viewModelScope.launch {
            saveForecastSettingMarkUseCase.value.execute(
                profile = state.value.currentProfile,
                forecastSetting = event.forecastSetting
            )
        }
    }

    private fun saveMapPoint(event: SavePoint) {
        viewModelScope.launch {
            saveMapPointUseCase.execute(
                pointName = event.title,
                profile = event.profile,
                latitude = event.latitude,
                longitude = event.longitude,
            )
        }
    }

    private fun selectProfile(name: Profile) {
        viewModelScope.launch {
            selectProfileUseCase.value.execute(name)
        }
    }

    private fun deleteProfile(name: Profile) {
        viewModelScope.launch {
            deleteProfileUseCase.value.execute(name)
        }

    }

    private fun createProfile(name: Profile) {
        viewModelScope.launch {
            saveProfileUseCase.execute(name)
        }
    }
}

data class MainViewState(
    val currentProfile: Profile = Profile("", isCommon = true),
    val mapPoints: List<MapPoint> = emptyList(),
    val profiles: List<Profile> = emptyList()
)


sealed class Event

class CreateProfile(val name: Profile) : Event()
class SelectProfile(val name: Profile) : Event()
class DeleteProfile(val name: Profile) : Event()

data class SavePoint(
    val title: String,
    val profile: Profile,
    val latitude: Double,
    val longitude: Double
) : Event()

data class SaveForecastSettingMark(
    val forecastSetting: ForecastSetting
) : Event()