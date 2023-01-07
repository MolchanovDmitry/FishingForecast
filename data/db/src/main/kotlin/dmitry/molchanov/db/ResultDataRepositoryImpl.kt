package dmitry.molchanov.db

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.repository.NullResultId
import dmitry.molchanov.domain.repository.NullWeatherData
import dmitry.molchanov.domain.repository.ResultDataRepository
import dmitry.molchanov.fishingforecast.mapper.MapPointMapper
import dmitry.molchanov.fishingforecast.mapper.ProfileMapper
import dmitry.molchanov.fishingforecast.model.Result
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    override suspend fun saveResult(
        resultName: String,
        weatherDataIds: List<Long>,
        profile: SimpleProfile?,
        mapPoint: MapPoint,
    ) {

        resultQueries.insert(
            name = resultName,
            profileName = profile?.name,
            mapPointId = mapPoint.id,
        )

        val resultId = resultQueries.lastInsertResultId().executeAsOneOrNull() ?: throw NullResultId()

        weatherDataIds.forEach { id ->
            resultToWeatherDataQueries.insert(
                ResultToWeatherData(resultId = resultId, weatherDataId = id)
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