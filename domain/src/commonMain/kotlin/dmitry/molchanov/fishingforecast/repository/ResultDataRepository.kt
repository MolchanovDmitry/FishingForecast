package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface ResultDataRepository {

    suspend fun saveResult(weatherData: List<WeatherData>, profile: SimpleProfile?, mapPoint: MapPoint)

    fun getResultsFlow(): Flow<List<dmitry.molchanov.fishingforecast.model.Result>>
}

class NullWeatherData : IllegalStateException("Weather date are null")
class NullResultId : IllegalStateException("Last inserted result row not founded")
