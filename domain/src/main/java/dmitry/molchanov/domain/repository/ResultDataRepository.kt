package dmitry.molchanov.domain.repository

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Result
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import kotlinx.coroutines.flow.Flow

interface ResultDataRepository {

    suspend fun saveResult(
        resultName: String,
        weatherDataIds: List<Long>,
        profile: SimpleProfile?,
        mapPoint: MapPoint,
    )

    fun getResultsFlow(): Flow<List<Result>>
    suspend fun getWeatherDataIdsByResult(result: Result): List<Long>
}

class NullWeatherData : IllegalStateException("Weather date are null")
class NullResultId : IllegalStateException("Last inserted result row not founded")
