package dmitry.molchanov.domain.repository

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.RawWeatherData
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.utils.TimeMs
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
    suspend fun getWeatherDataIds(weatherData: List<WeatherData>): List<Long>
}
