package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.utils.TimeMs

interface WeatherDataRepository {

    suspend fun fetchWeatherData(mapPoint: MapPoint, from: TimeMs, to: TimeMs): List<WeatherData>

    suspend fun saveWeatherData(weatherData: List<WeatherData>)
}