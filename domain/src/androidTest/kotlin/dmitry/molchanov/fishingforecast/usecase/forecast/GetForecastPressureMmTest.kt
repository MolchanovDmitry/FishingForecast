package dmitry.molchanov.fishingforecast.usecase.forecast

import dmitry.molchanov.fishingforecast.model.DeltaForecastMark
import dmitry.molchanov.fishingforecast.model.ForecastSettingsItem.PRESSURE_MM
import dmitry.molchanov.fishingforecast.model.MinValueForecastMark
import dmitry.molchanov.fishingforecast.model.Pressure
import org.junit.Test
import kotlin.test.assertEquals


class GetForecastPressureMmTest : BaseForecastTest() {

    @Test
    fun `check pressure mm min good value`() {
        val weatherData = listOf(getWeatherDate(pressure = Pressure(mm = 740F, pa = 1F)))
        val settings = getForecastSettings(PRESSURE_MM, MinValueForecastMark(value = 750F))
        val forecasts = getForecastUseCase.execute(weatherData, settings)
        forecasts.first().apply {
            assertEquals(PRESSURE_MM, forecastSettingsItem)
            assertEquals(true, isGood)
        }
        assertEquals(1, forecasts.size)
    }

    @Test
    fun `check pressure mm min bad value`() {
        val weatherData = listOf(getWeatherDate(pressure = Pressure(mm = 740F, pa = 1F)))
        val settings = getForecastSettings(PRESSURE_MM, MinValueForecastMark(value = 730F))
        val forecasts = getForecastUseCase.execute(weatherData, settings)
        forecasts.first().apply {
            assertEquals(PRESSURE_MM, forecastSettingsItem)
            assertEquals(false, isGood)
        }
        assertEquals(1, forecasts.size)
    }

    @Test
    fun `check pressure mm max good value`() {
        val weatherData = listOf(getWeatherDate(pressure = Pressure(mm = 740F, pa = 1F)))
        val settings = getForecastSettings(PRESSURE_MM, MinValueForecastMark(value = 740F))
        val forecasts = getForecastUseCase.execute(weatherData, settings)
        forecasts.first().apply {
            assertEquals(PRESSURE_MM, forecastSettingsItem)
            assertEquals(true, isGood)
        }
        assertEquals(1, forecasts.size)
    }

    @Test
    fun `check pressure mm max bad value`() {
        val weatherData = listOf(getWeatherDate(pressure = Pressure(mm = 740F, pa = 1F)))
        val settings = getForecastSettings(PRESSURE_MM, MinValueForecastMark(value = 720F))
        val forecasts = getForecastUseCase.execute(weatherData, settings)
        forecasts.first().apply {
            assertEquals(PRESSURE_MM, forecastSettingsItem)
            assertEquals(false, isGood)
        }
        assertEquals(1, forecasts.size)
    }

    @Test
    fun `check pressure mm delta good result`() {
        val weatherData = listOf(
            getWeatherDate(pressure = Pressure(mm = 740F, pa = 1F)),
            getWeatherDate(pressure = Pressure(mm = 760F, pa = 1F)),
        )
        val settings = getForecastSettings(PRESSURE_MM, DeltaForecastMark(value = 30F))
        val forecasts = getForecastUseCase.execute(weatherData, settings)
        forecasts.first().apply {
            assertEquals(PRESSURE_MM, forecastSettingsItem)
            assertEquals(true, isGood)
        }
        assertEquals(1, forecasts.size)
    }

    @Test
    fun `check pressure mm delta bad result`() {
        val weatherData = listOf(
            getWeatherDate(pressure = Pressure(mm = 740F, pa = 1F)),
            getWeatherDate(pressure = Pressure(mm = 760F, pa = 1F)),
        )
        val settings = getForecastSettings(PRESSURE_MM, DeltaForecastMark(value = 10F))
        val forecasts = getForecastUseCase.execute(weatherData, settings)
        forecasts.first().apply {
            assertEquals(PRESSURE_MM, forecastSettingsItem)
            assertEquals(false, isGood)
        }
        assertEquals(1, forecasts.size)
    }
}