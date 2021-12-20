package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.commonProfile
import dmitry.molchanov.fishingforecast.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    getProfilesUseCase: GetProfilesUseCase,
    getMapPointsUseCase: GetMapPointsUseCase,
    getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val saveMapPointUseCase: SaveMapPointUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val selectProfileUseCase: SelectProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    init {
        getMapPointsUseCase.execute()
            .onEach {
                _state.value = state.value.copy(mapPoints = it)
            }
            .launchIn(viewModelScope)
        getProfilesUseCase.execute()
            .onEach {
                _state.value = state.value.copy(profiles = it)
            }.launchIn(viewModelScope)

        getCurrentProfileUseCase.execute()
            .onEach {
                _state.value = state.value.copy(currentProfile = it)
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is SavePoint -> saveMapPoint(event)
            is CreateProfile -> createProfile(event.name)
            is DeleteProfile -> deleteProfile(event.name)
            is SelectProfile -> selectProfile(event.name)
        }
    }

    private fun saveMapPoint(event: SavePoint) {
        viewModelScope.launch {
            saveMapPointUseCase.execute(
                Profile(""),
                MapPoint(
                    "",
                    profileName = "",
                    latitude = event.latitude,
                    longitude = event.longitude
                )
            )
        }
    }

    private fun selectProfile(name: Profile) {
        viewModelScope.launch {
            selectProfileUseCase.execute(name)
        }
    }

    private fun deleteProfile(name: Profile) {
        viewModelScope.launch {
            deleteProfileUseCase.execute(name)
        }

    }

    private fun createProfile(name: Profile) {
        viewModelScope.launch {
            saveProfileUseCase.execute(name)
        }
    }
}

data class MainViewState(
    val currentProfile: Profile = commonProfile,
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