package dmitry.molchanov.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dmitry.molchanov.core.DispatcherDefault
import dmitry.molchanov.core.DispatcherIo
import dmitry.molchanov.domain.mapper.MapPointMapper
import dmitry.molchanov.domain.mapper.ProfileMapper
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.NullResultId
import dmitry.molchanov.domain.repository.NullWeatherData
import dmitry.molchanov.domain.repository.ResultDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import dmitry.molchanov.db.Result as DataResult

class ResultDataRepositoryImpl(
    private val resultQueries: ResultQueries,
    private val profileMapper: ProfileMapper,
    private val mapPointMapper: MapPointMapper,
    private val resultToWeatherDataQueries: ResultToWeatherDataQueries,
    private val mapDispatcher: DispatcherDefault,
    private val dispatcherIo: DispatcherIo,
) : ResultDataRepository {

    override fun getResultsFlow(): Flow<List<Result>> =
        resultQueries.selectAll()
            .asFlow()
            .flowOn(dispatcherIo)
            .mapToList(mapDispatcher)
            .map { dataResults -> mapToResult(dataResults) }
            .flowOn(mapDispatcher)


    // TODO сделать сохранение точки не только начиная с текущего дня
    @Throws(NullWeatherData::class, NullResultId::class)
    override suspend fun saveResult(
        resultName: String,
        weatherDataIds: List<Long>,
        profile: SimpleProfile?,
        mapPoint: MapPoint,
    ) = withContext(dispatcherIo) {

        resultQueries.insert(
            name = resultName,
            profileName = profile?.name,
            mapPointId = mapPoint.id,
        )

        val resultId =
            resultQueries.lastInsertResultId().executeAsOneOrNull() ?: throw NullResultId()

        weatherDataIds.forEach { id ->
            resultToWeatherDataQueries.insert(
                ResultToWeatherData(resultId = resultId, weatherDataId = id)
            )
        }
    }

    override suspend fun getWeatherDataIdsByResult(result: Result): List<Long> =
        withContext(dispatcherIo) {
            resultToWeatherDataQueries.selectWeatherDataResultId(resultId = result.id)
                .executeAsList()
        }

    private suspend fun mapToResult(dataResults: List<DataResult>): List<Result> =
        withContext(dispatcherIo) {
            dataResults.mapNotNull { resultItem ->
                val mapPoint = mapPointMapper.getMapPointById(resultItem.mapPointId)
                mapPoint?.let {
                    Result(
                        id = resultItem.id,
                        mapPoint = mapPoint,
                        name = resultItem.name
                    )
                }
            }
        }
}