package dmitry.molchanov.db

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.Result as DataResult
import dmitry.molchanov.domain.mapper.MapPointMapper
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.NullResultId
import dmitry.molchanov.domain.repository.NullWeatherData
import dmitry.molchanov.domain.repository.ResultDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ResultDataRepositoryImpl(
    private val resultQueries: ResultQueries,
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
        rating: Int?,
        description: String?
    ) {

        resultQueries.insert(
            name = resultName,
            profileName = profile?.name,
            mapPointId = mapPoint.id,
            rating = rating?.toLong(),
            description = description
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

    override suspend fun getMinWeatherDateByResult(resultId: Long): Long? =
        resultToWeatherDataQueries.selectMinWeatherDateByResult(resultId = resultId)
            .executeAsOneOrNull()?.MIN

    override suspend fun getMaxWeatherDateByResult(resultId: Long): Long? =
        resultToWeatherDataQueries.selectMaxWeatherDateByResult(resultId = resultId)
            .executeAsOneOrNull()?.MAX

    override suspend fun updateResultName(resultId: Long, newName: String) {
        resultQueries.updateName(name = newName, id = resultId)
    }

    override suspend fun updateResultRating(resultId: Long, rating: Int) {
        resultQueries.updateRating(rating = rating.toLong(), id = resultId)
    }

    override suspend fun updateResultDescription(resultId: Long, description: String) {
        resultQueries.updateDescription(description = description, id = resultId)
    }

    override fun getResultsFlowOrderByDate(): Flow<List<Result>> =
        resultQueries.selectAllOrderByDate()
            .asFlow()
            .mapToList()
            .map { dataResults -> mapToResult(dataResults) }

    override fun getResultsFlowOrderByRating(): Flow<List<Result>> =
        resultQueries.selectAllOrderByRating()
            .asFlow()
            .mapToList()
            .map { dataResults -> mapToResult(dataResults) }

    private suspend fun mapToResult(dataResults: List<DataResult>): List<Result> =
        dataResults.mapNotNull { resultItem ->
            val mapPoint = mapPointMapper.getMapPointById(resultItem.mapPointId)
            mapPoint?.let {
                Result(
                    id = resultItem.id,
                    mapPoint = mapPoint,
                    name = resultItem.name,
                    rating = resultItem.rating?.toInt(),
                    description = resultItem.description
                )
            }
        }
}
