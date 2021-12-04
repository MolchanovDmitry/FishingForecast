package dmitry.molchanov.fishingforecast.usecase.forecast

import dmitry.molchanov.fishingforecast.model.ExactValueForecastMark
import dmitry.molchanov.fishingforecast.model.ForecastSettingsItem.OBSERVATION_PERIOD
import dmitry.molchanov.fishingforecast.utils.UnixTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertEquals

class GetForecastDayTest : BaseForecastTest() {

    @Test
    fun `check good forecast period`() {
        val weatherData = listOf(
            getWeatherDate(date = getUnixDate(1)),
            getWeatherDate(date = getUnixDate(2)),
            getWeatherDate(date = getUnixDate(3)),
            getWeatherDate(date = getUnixDate(4)),
            getWeatherDate(date = getUnixDate(5))
        )
        val settings = getForecastSettings(OBSERVATION_PERIOD, ExactValueForecastMark(value = 6F))
        val forecasts = getForecastUseCase.execute(weatherData, settings)
        forecasts.first().apply {
            assertEquals(OBSERVATION_PERIOD, forecastSettingsItem)
            assertEquals(true, isGood)
        }
        assertEquals(1, forecasts.size)
    }

    @Test
    fun `check bad forecast period`() {
        val weatherData = listOf(
            getWeatherDate(date = getUnixDate(1)),
            getWeatherDate(date = getUnixDate(2)),
            getWeatherDate(date = getUnixDate(3)),
            getWeatherDate(date = getUnixDate(4)),
            getWeatherDate(date = getUnixDate(5))
        )
        val settings = getForecastSettings(OBSERVATION_PERIOD, ExactValueForecastMark(value = 3F))
        val forecasts = getForecastUseCase.execute(weatherData, settings)
        forecasts.first().apply {
            assertEquals(OBSERVATION_PERIOD, forecastSettingsItem)
            assertEquals(false, isGood)
        }
        assertEquals(1, forecasts.size)
    }

    private fun getUnixDate(dayNumber: Int): UnixTime =
        LocalDateTime(
            year = 2016,
            monthNumber = 2,
            dayOfMonth = dayNumber,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        ).toInstant(TimeZone.UTC).epochSeconds
}