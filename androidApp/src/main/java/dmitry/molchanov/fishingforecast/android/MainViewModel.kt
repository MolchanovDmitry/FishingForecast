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

    private fun selectProfile(name: String) {
        viewModelScope.launch {
            selectProfileUseCase.execute(Profile(name))
        }
    }

    private fun deleteProfile(name: String) {
        viewModelScope.launch {
            deleteProfileUseCase.execute(Profile(name))
        }

    }

    private fun createProfile(name: String) {
        viewModelScope.launch {
            saveProfileUseCase.execute(Profile(name))
        }
    }
}

data class MainViewState(
    val currentProfile: Profile = commonProfile,
    val mapPoints: List<MapPoint> = emptyList(),
    val profiles: List<Profile> = emptyList()
)


sealed class Event

class CreateProfile(val name: String) : Event()
class SelectProfile(val name: String) : Event()
class DeleteProfile(val name: String) : Event()
data class SavePoint(val latitude: Double, val longitude: Double) : Event()