package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.commonProfile
import dmitry.molchanov.fishingforecast.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    getProfilesUseCase: GetProfilesUseCase,
    getMapPointsUseCase: GetMapPointsUseCase,
    //getCurrentProfileUseCase: GetCurrentProfileUseCase,
    //private val saveProfileUseCase: SaveProfileUseCase,
    private val saveMapPointUseCase: SaveMapPointUseCase,
    private val deleteProfileUseCase: Lazy<DeleteProfileUseCase>,
    private val selectProfileUseCase: Lazy<SelectProfileUseCase>,
    private val saveForecastSettingMarkUseCase: Lazy<SaveForecastSettingMarkUseCase>,
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    init {
        val mapPointFlow = getMapPointsUseCase.execute()

        /*getCurrentProfileUseCase.execute()
            .onEach {
                TODO("Разобраться, почему collect собирает невалидные данные")
                _state.value = state.value.copy(
                    currentProfile = it,
                    mapPoints = mapPointFlow.last().let(::mapPointsByCurrentProfile)
                )
            }
            .launchIn(viewModelScope)*/

        getProfilesUseCase.execute()
            .onEach {
                _state.value = state.value.copy(profiles = it)
            }.launchIn(viewModelScope)

        mapPointFlow
            .onEach {
                _state.value = state.value.copy(mapPoints = it)
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
            saveForecastSettingMarkUseCase.value.execute(Profile(""), event.forecastSetting)
        }
    }

    private fun saveMapPoint(event: SavePoint) {
        viewModelScope.launch {
            saveMapPointUseCase.execute(
                MapPoint(
                    name = event.title,
                    latitude = event.latitude,
                    longitude = event.longitude,
                    profileName = event.profile.name,
                )
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
            //saveProfileUseCase.execute(name)
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

data class SaveForecastSettingMark(
    val forecastSetting: ForecastSetting
) : Event()