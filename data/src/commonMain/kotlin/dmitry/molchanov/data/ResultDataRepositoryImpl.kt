package dmitry.molchanov.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.ResultQueries
import dmitry.molchanov.db.ResultToWeatherData
import dmitry.molchanov.db.ResultToWeatherDataQueries
import dmitry.molchanov.fishingforecast.mapper.MapPointMapper
import dmitry.molchanov.fishingforecast.mapper.ProfileMapper
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Result
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.NullResultId
import dmitry.molchanov.fishingforecast.repository.NullWeatherData
import dmitry.molchanov.fishingforecast.repository.ResultDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import dmitry.molchanov.db.Result as DataResult

class ResultDataRepositoryImpl(
    private val resultQueries: ResultQueries,
    private val profileMapper: ProfileMapper,
    private val mapPointMapper: MapPointMapper,
    private val resultToWeatherDataQueries: ResultToWeatherDataQueries,
) : ResultDataRepository {

    override fun getResultsFlow(): Flow<List<Result>> =
        resultQueries.selectAll()
            .asFlow()
            .mapToList()
            .map { dataResults -> mapToResult(dataResults) }


    // TODO сделать сохранение точки не только начиная с текущего дня
    @Throws(NullWeatherData::class, NullResultId::class)
    override suspend fun saveResult(weatherData: List<WeatherData>, profile: SimpleProfile?, mapPoint: MapPoint) {

        resultQueries.insert(
            name = UUID.randomUUID().toString(),
            profileName = profile?.name,
            mapPointId = mapPoint.id,
        )

        val resultId = resultQueries.lastInsertResultId().executeAsOneOrNull() ?: throw NullResultId()

        weatherData.forEach { weatherDataItem ->
            resultToWeatherDataQueries.insert(
                ResultToWeatherData(resultId = resultId, weatherDataId = weatherDataItem.id)
            )
        }
    }

    override suspend fun getWeatherDataIdsByResult(result: Result): List<Long> =
        resultToWeatherDataQueries.selectWeatherDataResultId(resultId = result.id)
            .executeAsList()

    private suspend fun mapToResult(dataResults: List<DataResult>): List<Result> =
        dataResults.mapNotNull { resultItem ->
            val mapPoint = mapPointMapper.getMapPointById(resultItem.mapPointId)
            mapPoint?.let { Result(id = resultItem.id, mapPoint = mapPoint, name = resultItem.name) }
        }
}