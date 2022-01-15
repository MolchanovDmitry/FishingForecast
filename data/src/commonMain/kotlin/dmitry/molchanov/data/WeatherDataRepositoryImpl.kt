package dmitry.molchanov.data

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.model.mockWeatherData
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.utils.TimeMs

class WeatherDataRepositoryImpl:WeatherDataRepository {
    override suspend fun fetchWeatherData(
        mapPoint: MapPoint,
        from: TimeMs,
        to: TimeMs
    ): List<WeatherData> {
        return mockWeatherData
    }

    override suspend fun saveWeatherData(weatherData: List<WeatherData>) {
        TODO("Not yet implemented")
    }
}