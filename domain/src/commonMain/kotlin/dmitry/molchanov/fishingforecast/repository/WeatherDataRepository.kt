package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.RawWeatherData
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.utils.TimeMs
import kotlinx.coroutines.flow.Flow

interface WeatherDataRepository {

    fun fetchAllWeatherData(): Flow<List<WeatherData>>

    fun fetchWeatherDataFlow(
        mapPoint: MapPoint,
        from: TimeMs,
        to: TimeMs
    ): Flow<List<WeatherData>>

    suspend fun saveRawWeatherData(weatherData: List<RawWeatherData>)

    suspend fun saveWeatherData(weatherData: List<WeatherData>)
    suspend fun fetchWeatherData(mapPoint: MapPoint, from: TimeMs, to: TimeMs): List<WeatherData>
    suspend fun getWeatherDataByIds(ids: List<Long>): List<WeatherData>
}