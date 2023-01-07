package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.model.ExactValueForecastMark
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.ForecastSettingsItem
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.WeatherDataRepository
import dmitry.molchanov.domain.usecase.GetForecastSettingMarksUseCase
import dmitry.molchanov.domain.usecase.GetForecastUseCase
import dmitry.molchanov.domain.utils.ONE_SEC
import dmitry.molchanov.fishingforecast.model.Forecast
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherStatisticViewState(
    val weatherData: List<WeatherData> = emptyList(),
    val forecasts: List<Forecast> = emptyList()
)

class WeatherStatisticViewModel(
    private val mapPoint: MapPoint,
    getProfilesUseCase: GetProfilesUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val weatherDataRepository: WeatherDataRepository,
    private val getForecastSettingMarksUseCase: GetForecastSettingMarksUseCase
) : ViewModel() {

    private val state = MutableStateFlow(WeatherStatisticViewState())
    var stateFlow = state.asStateFlow()
    private val message = MutableSharedFlow<String>()
    var messageFlow = message.asSharedFlow()

    init {
        viewModelScope.launch {
            getProfilesUseCase.execute().firstOrNull { profile -> profile == mapPoint.profile }
                ?.let { profile ->
                    val forecastSettings = getForecastSettings(profile)
                    getObservationPeriod(forecastSettings)?.let { period ->
                        observeWeather(
                            period, forecastSettings
                        )
                    } ?: message.emit("Не найден период прогнозирования для профиля")
                } ?: message.emit("Не найден период прогнозирования для профиля")
        }
    }

    private suspend fun getForecastSettings(profile: Profile): List<ForecastSetting> {
        return getForecastSettingMarksUseCase.execute(profile)
    }

    private fun getObservationPeriod(forecastSettings: List<ForecastSetting>): Period? {
        forecastSettings.firstOrNull { it.forecastSettingsItem == ForecastSettingsItem.OBSERVATION_PERIOD }
            ?.let { forecastSetting ->
                forecastSetting.forecastMarks.filterIsInstance<ExactValueForecastMark>()
                    .firstOrNull()?.let { forecastMark ->
                        val days = forecastMark.value
                        val from =
                            System.currentTimeMillis() - (days * 24 * 60 * 60 * ONE_SEC).toLong()
                        return Period(from = from, to = System.currentTimeMillis())
                    }
            }
        return null
    }

    private fun observeWeather(period: Period, forecastSettings: List<ForecastSetting>) {
        weatherDataRepository.fetchWeatherDataFlow(
            mapPoint = mapPoint, from = period.from, to = period.to
        ).onEach { weatherData ->
            state.update {
                val forecasts = getForecastUseCase.execute(weatherData, forecastSettings)
                it.copy(weatherData = weatherData, forecasts = forecasts)
            }
        }.launchIn(viewModelScope)
    }

    private data class Period(val from: Long, val to: Long)
}
