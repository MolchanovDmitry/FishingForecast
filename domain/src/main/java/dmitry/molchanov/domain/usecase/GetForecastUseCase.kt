package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.DeltaForecastMark
import dmitry.molchanov.domain.model.ExactValueForecastMark
import dmitry.molchanov.domain.model.Forecast
import dmitry.molchanov.domain.model.ForecastMark
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.ForecastSettingsItem
import dmitry.molchanov.domain.model.ForecastSettingsItem.HUMIDITY
import dmitry.molchanov.domain.model.ForecastSettingsItem.OBSERVATION_PERIOD
import dmitry.molchanov.domain.model.ForecastSettingsItem.PRESSURE_MM
import dmitry.molchanov.domain.model.ForecastSettingsItem.PRESSURE_PA
import dmitry.molchanov.domain.model.ForecastSettingsItem.TEMPERATURE_AVG
import dmitry.molchanov.domain.model.ForecastSettingsItem.TEMPERATURE_MAX
import dmitry.molchanov.domain.model.ForecastSettingsItem.TEMPERATURE_MIN
import dmitry.molchanov.domain.model.ForecastSettingsItem.TEMPERATURE_WATER
import dmitry.molchanov.domain.model.ForecastSettingsItem.WIND_SPEED
import dmitry.molchanov.domain.model.MaxValueForecastMark
import dmitry.molchanov.domain.model.MinValueForecastMark
import dmitry.molchanov.domain.model.WeatherData

/**
 * Сделать прогноз.
 */
class GetForecastUseCase {

    /**
     * @param weatherData данные погоды.
     * @param forecastSettings настройки прогнозирования.
     */
    fun execute(
        weatherData: List<WeatherData>,
        forecastSettings: List<ForecastSetting>
    ): List<Forecast> {
        return forecastSettings.flatMap {
            val settingsItem = it.forecastSettingsItem
            it.forecastMarks.map { mark ->
                if (settingsItem == OBSERVATION_PERIOD) {
                    getForecastForPeriodObservation(weatherData, mark)
                } else {
                    getForecastForMinMaxDeltaValues(settingsItem, weatherData, mark)
                }
            }
        }
    }

    /** Получить прогноз по периоду наблюдения. */
    private fun getForecastForPeriodObservation(
        weatherData: List<WeatherData>,
        mark: ForecastMark,
    ): Forecast {
        val realPeriod = getPeriodObservation(weatherData)
        val checkValue = (mark as? ExactValueForecastMark)?.value
            ?: error("OBSERVATION_PERIOD должен содержать только ExactValueForecastMark")
        return Forecast(
            forecastSettingsItem = OBSERVATION_PERIOD,
            isGood = realPeriod >= checkValue
        )
    }

    /** Получить количество дней из списка прогноза. */
    private fun getPeriodObservation(weatherData: List<WeatherData>): Int {
        if (weatherData.isEmpty()) return 0
        val firstDay = weatherData.first().date.roundedValue
        val lastDay = weatherData.last().date.roundedValue
        val secondDifference = lastDay - firstDay
        val minutes = secondDifference / 60
        val hours = minutes / 60
        val days = hours / 24
        return days.toInt()
    }

    /** Получить прогноз для показателей, которые могут содержать минимальные, максимальные и дельта значения. */
    private fun getForecastForMinMaxDeltaValues(
        forecastSettingsItem: ForecastSettingsItem,
        weatherData: List<WeatherData>,
        mark: ForecastMark
    ): Forecast {
        val values = getWeatherItemValues(forecastSettingsItem, weatherData)
        val minValue = values.minOrNull() ?: return Forecast(forecastSettingsItem, false)
        val maxValue = values.maxOrNull() ?: return Forecast(forecastSettingsItem, false)
        val realDelta = maxValue - minValue
        when (mark) {
            is MinValueForecastMark -> {
                if (minValue > mark.value)
                    return Forecast(forecastSettingsItem, false)
            }
            is MaxValueForecastMark -> {
                if (maxValue < mark.value)
                    return Forecast(forecastSettingsItem, false)
            }
            is DeltaForecastMark -> {
                if (realDelta > mark.value)
                    return Forecast(forecastSettingsItem, false)
            }
            else -> error("Недопустимый вид прогнозирования для ${mark::class.simpleName}")
        }
        return Forecast(forecastSettingsItem, true)
    }

    /** Получить значения для список показателей [forecastSettingsItem]. */
    private fun getWeatherItemValues(
        forecastSettingsItem: ForecastSettingsItem,
        weatherData: List<WeatherData>
    ): List<Float> =
        when (forecastSettingsItem) {
            PRESSURE_MM -> weatherData.mapNotNull { it.pressure?.mm }
            PRESSURE_PA -> weatherData.mapNotNull { it.pressure?.pa }
            WIND_SPEED -> weatherData.mapNotNull { it.wind?.speed }
            TEMPERATURE_MIN -> weatherData.mapNotNull { it.temperature?.min }
            TEMPERATURE_AVG -> weatherData.mapNotNull { it.temperature?.avg }
            TEMPERATURE_MAX -> weatherData.mapNotNull { it.temperature?.max }
            TEMPERATURE_WATER -> weatherData.mapNotNull { it.temperature?.water }
            HUMIDITY -> weatherData.mapNotNull { it.humidity }
            else -> error("Недопустимый элемент наблюдения $forecastSettingsItem")
        }
}
