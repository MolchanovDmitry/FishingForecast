package dmitry.molchanov.fishingforecast.usecase.forecast

import dmitry.molchanov.fishingforecast.model.*
import dmitry.molchanov.fishingforecast.usecase.GetForecastUseCase

open class BaseForecastTest {

    protected val getForecastUseCase = GetForecastUseCase()

    protected fun getForecastSettings(
        forecastSettingItem: ForecastSettingsItem,
        forecastMark: ForecastMark
    ) = listOf(ForecastSetting(forecastSettingItem, forecastMark))

    protected fun getWeatherDate(
        date: Long = 1,
        mapPoint: MapPoint = MapPoint("", 1, 1),
        pressure: Pressure = Pressure(1F, 1F),
        temperature: Temperature = Temperature(1F, 1F, 1F),
        temperatureWater: Float = 1F,
        wind: Wind = Wind(1F, 1F, ""),
        humidity: Float = 1F
    ) =
        WeatherData(
            date = date,
            mapPoint = mapPoint,
            pressure = pressure,
            temperature = temperature,
            temperatureWater = temperatureWater,
            wind = wind,
            humidity = humidity,
        )
}