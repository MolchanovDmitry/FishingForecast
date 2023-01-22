package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.model.CommonProfile
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.WeatherDataRepository
import dmitry.molchanov.domain.usecase.DeleteForecastSettingUseCase
import dmitry.molchanov.domain.usecase.GetCurrentProfileUseCase
import dmitry.molchanov.domain.usecase.GetForecastSettingMarksUseCase
import dmitry.molchanov.domain.usecase.SaveForecastSettingMarkUseCase
import dmitry.molchanov.domain.usecase.SaveWeatherDataUseCase
import dmitry.molchanov.fishingforecast.mapper.CommonProfileFetcher
import dmitry.molchanov.fishingforecast.repository.YandexWeatherRepository
import dmitry.molchanov.fishingforecast.usecase.GetMapPointsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    getMapPointsUseCase: GetMapPointsUseCase,
    commonProfileFetcher: CommonProfileFetcher,
    getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val saveWeatherDataUseCase: SaveWeatherDataUseCase,
    private val deleteForecastSettings: Lazy<DeleteForecastSettingUseCase>,
    private val getForecastSettingMarks: GetForecastSettingMarksUseCase,
    private val saveForecastSettingMarkUseCase: Lazy<SaveForecastSettingMarkUseCase>,
    private val yandexWeatherRepository: YandexWeatherRepository,
    private val weatherDataRepository: WeatherDataRepository,
) : ViewModel() {

    private val _state =
        MutableStateFlow(MainViewState(currentProfile = commonProfileFetcher.instance))
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    private var allMapPoints: List<MapPoint> = emptyList()

    init {
        getCurrentProfileUseCase.execute().onEach { profile ->
            _state.update { mainViewState ->
                mainViewState.copy(
                    currentProfile = profile,
                    mapPoints = if (profile is CommonProfile) {
                        allMapPoints
                    } else {
                        allMapPoints.filter { it.profile == profile }
                    }
                )
            }
            updateForecastSettings(profile)
        }.launchIn(viewModelScope)

        getMapPointsUseCase.executeFlow().onEach { mapPoints ->
            this.allMapPoints = mapPoints
            _state.update { mainViewState ->
                mainViewState.copy(
                    mapPoints = if (state.value.currentProfile is CommonProfile) {
                        mapPoints
                    } else {
                        mapPoints.filter { it.profile == state.value.currentProfile }
                    }
                )
            }
        }.launchIn(viewModelScope)

        weatherDataRepository.fetchAllWeatherData().onEach { weatherData ->
            _state.update { it.copy(weatherData = weatherData) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is DeleteForecastSetting -> deleteForecastSetting(event)
            is SaveForecastSettingMark -> saveForecastSettingMark(event)
            FetchWeatherData -> fetchWeatherData()
        }
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            state.value.mapPoints.forEach { mapPoint ->
                saveWeatherDataUseCase.execute(mapPoint)
            }
        }
    }

    private fun deleteForecastSetting(event: DeleteForecastSetting) {
        viewModelScope.launch {
            deleteForecastSettings.value.execute(
                profile = state.value.currentProfile, forecastSetting = event.forecastSetting
            )
        }
    }

    private var updateForecastJob: Job? = null

    private fun updateForecastSettings(profile: Profile) {
        updateForecastJob?.cancel()
        updateForecastJob =
            getForecastSettingMarks.executeFlow(profile).onEach { forecastSettings ->
                _state.update { it.copy(forecastSettings = forecastSettings) }
            }.launchIn(viewModelScope)
    }

    private fun saveForecastSettingMark(event: SaveForecastSettingMark) {
        viewModelScope.launch {
            saveForecastSettingMarkUseCase.value.execute(
                profile = state.value.currentProfile as? SimpleProfile,
                forecastSetting = event.forecastSetting
            )
        }
    }
}

data class MainViewState(
    val currentProfile: Profile,
    val mapPoints: List<MapPoint> = emptyList(),
    val profiles: List<Profile> = emptyList(),
    val forecastSettings: List<ForecastSetting> = emptyList(),
    val weatherData: List<WeatherData> = emptyList()
)

sealed class Event

object FetchWeatherData : Event()
class DeleteForecastSetting(val forecastSetting: ForecastSetting) : Event()

data class SaveForecastSettingMark(
    val forecastSetting: ForecastSetting
) : Event()
