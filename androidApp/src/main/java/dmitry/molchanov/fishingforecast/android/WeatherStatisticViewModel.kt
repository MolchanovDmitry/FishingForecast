package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.model.*
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.usecase.GetForecastSettingMarksUseCase
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import dmitry.molchanov.fishingforecast.utils.ONE_SEC
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class WeatherStatisticViewState(
    val weatherData: List<WeatherData> = emptyList()
)

class WeatherStatisticViewModel(
    private val mapPoint: MapPoint,
    getProfilesUseCase: GetProfilesUseCase,
    private val weatherDataRepository: WeatherDataRepository,
    private val getForecastSettingMarksUseCase: GetForecastSettingMarksUseCase
) : ViewModel() {

    private val state = MutableStateFlow(WeatherStatisticViewState())
    var stateFlow = state.asStateFlow()

    init {
        viewModelScope.launch {
            getProfilesUseCase.execute()
                .firstOrNull { (it.isCommon && mapPoint.profileName == null) || (it.name == mapPoint.profileName) }
                ?.let { profile ->
                    val period = getObservationPeriod(profile)
                        ?: error("Не найден период прогнозирования для профиля")
                    observeWeather(period)
                }
                ?: error("Нет совпадения по профилю.")
        }
    }

    private suspend fun getObservationPeriod(profile: Profile): Period? {
        getForecastSettingMarksUseCase.execute(profile)
            .firstOrNull { it.forecastSettingsItem == ForecastSettingsItem.OBSERVATION_PERIOD }
            ?.let { forecastSetting ->
                forecastSetting.forecastMarks
                    .filterIsInstance<ExactValueForecastMark>()
                    .firstOrNull()
                    ?.let { forecastMark ->
                        val days = forecastMark.value
                        val from =
                            System.currentTimeMillis() - (days * 24 * 60 * 60 * ONE_SEC).toLong()
                        return Period(from = from, to = System.currentTimeMillis())
                    }
            }
        return null
    }

    private fun observeWeather(period: Period) {
        weatherDataRepository.fetchWeatherData(
            mapPoint = mapPoint,
            from = period.from,
            to = period.to
        ).onEach { weatherData ->
            state.update { it.copy(weatherData = weatherData) }
        }.launchIn(viewModelScope)
    }

    private data class Period(val from: Long, val to: Long)

}

