package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.repository.YandexWeatherRepository
import dmitry.molchanov.fishingforecast.usecase.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    getProfilesUseCase: GetProfilesUseCase,
    getMapPointsUseCase: GetMapPointsUseCase,
    getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val saveMapPointUseCase: SaveMapPointUseCase,
    private val getSavedWeatherData: GetSavedWeatherDataUseCase,
    private val deleteProfileUseCase: Lazy<DeleteProfileUseCase>,
    private val selectProfileUseCase: Lazy<SelectProfileUseCase>,
    private val deleteForecastSettings: Lazy<DeleteForecastSettingUseCase>,
    private val getForecastSettingMarks: GetForecastSettingMarksUseCase,
    private val saveForecastSettingMarkUseCase: Lazy<SaveForecastSettingMarkUseCase>,
    private val yandexWeatherRepository: YandexWeatherRepository,
    private val weatherDataRepository: WeatherDataRepository
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
                updateForecastSettings(profile)
            }
            .launchIn(viewModelScope)

        getProfilesUseCase.executeFlow()
            .onEach { profiles ->
                _state.update { it.copy(profiles = profiles) }
            }.launchIn(viewModelScope)

        getMapPointsUseCase.executeFlow()
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

        weatherDataRepository.fetchAllWeatherData()
            .onEach { weatherData ->
                _state.update { it.copy(weatherData = weatherData) }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is SavePoint -> saveMapPoint(event)
            is CreateProfile -> createProfile(event.name)
            is DeleteProfile -> deleteProfile(event.name)
            is SelectProfile -> selectProfile(event.name)
            is DeleteForecastSetting -> deleteForecastSetting(event)
            is SaveForecastSettingMark -> saveForecastSettingMark(event)
            FetchWeatherData -> fetchWeatherData()
        }
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            state.value.mapPoints.forEach { mapPoint ->
                yandexWeatherRepository.getYandexWeatherDate(mapPoint)
                    .onFailure { it.printStackTrace() }
                    .getOrNull()
                    ?.let { weatherData ->
                        weatherDataRepository.saveWeatherData(weatherData)
                    }
            }
        }
    }

    private fun deleteForecastSetting(event: DeleteForecastSetting) {
        viewModelScope.launch {
            deleteForecastSettings.value.execute(
                profile = state.value.currentProfile,
                forecastSetting = event.forecastSetting
            )
        }
    }

    private var updateForecastJob: Job? = null

    private fun updateForecastSettings(profile: Profile) {
        updateForecastJob?.cancel()
        updateForecastJob = getForecastSettingMarks.executeFlow(profile)
            .onEach { forecastSettings ->
                _state.update { it.copy(forecastSettings = forecastSettings) }
            }
            .launchIn(viewModelScope)
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
    val profiles: List<Profile> = emptyList(),
    val forecastSettings: List<ForecastSetting> = emptyList(),
    val weatherData: List<WeatherData> = emptyList()
)


sealed class Event

object FetchWeatherData : Event()
class CreateProfile(val name: Profile) : Event()
class SelectProfile(val name: Profile) : Event()
class DeleteProfile(val name: Profile) : Event()
class DeleteForecastSetting(val forecastSetting: ForecastSetting) : Event()

data class SavePoint(
    val title: String,
    val profile: Profile,
    val latitude: Double,
    val longitude: Double
) : Event()

data class SaveForecastSettingMark(
    val forecastSetting: ForecastSetting
) : Event()