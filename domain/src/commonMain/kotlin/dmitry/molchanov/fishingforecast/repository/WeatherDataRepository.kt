package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.utils.UnixTime

interface WeatherDataRepository {

    suspend fun fetchWeatherData(mapPoint: MapPoint, from: UnixTime, to: UnixTime): List<WeatherData>

    suspend fun saveWeatherData(weatherData: List<WeatherData>)
}