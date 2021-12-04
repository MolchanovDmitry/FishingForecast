package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.*
import dmitry.molchanov.fishingforecast.model.ForecastSettingsItem.*

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
        return forecastSettings.map {
            val settingsItem = it.forecastSettingsItem
            val mark = it.forecastMark

            if (settingsItem == OBSERVATION_PERIOD) {
                getForecastForPeriodObservation(weatherData, mark)
            } else {
                getForecastForMinMaxDeltaValues(settingsItem, weatherData, mark)
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
        val firstDay = weatherData.first().date
        val lastDay = weatherData.last().date
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
            PRESSURE_MM -> weatherData.map { it.pressure.mm }
            PRESSURE_PA -> weatherData.map { it.pressure.pa }
            WIND_SPEED -> weatherData.map { it.wind.speed }
            TEMPERATURE_MIN -> weatherData.map { it.temperature.min }
            TEMPERATURE_AVG -> weatherData.map { it.temperature.avg }
            TEMPERATURE_MAX -> weatherData.map { it.temperature.max }
            TEMPERATURE_WATER -> weatherData.map { it.temperatureWater }
            HUMIDITY -> weatherData.map { it.humidity }
            else -> error("Недопустимый элемент наблюдения $forecastSettingsItem")
        }
}