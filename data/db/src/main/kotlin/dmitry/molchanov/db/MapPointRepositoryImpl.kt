package dmitry.molchanov.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dmitry.molchanov.core.DispatcherDefault
import dmitry.molchanov.core.DispatcherIo
import dmitry.molchanov.domain.mapper.ProfileMapper
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.MapPointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import dmitry.molchanov.db.MapPoint as DataMapPoint
import dmitry.molchanov.domain.model.MapPoint as DomainMapPoint

class MapPointRepositoryImpl(
    private val profileMapper: ProfileMapper,
    private val mapPointQueries: MapPointQueries,
    private val mapDispatcher: DispatcherDefault,
    private val dispatcherIo: DispatcherIo,
) : MapPointRepository {

    override fun fetchMapPointsFlow(): Flow<List<DomainMapPoint>> {
        return mapPointQueries.selectAll()
            .asFlow()
            .flowOn(dispatcherIo)
            .mapToList(mapDispatcher)
            .map(::mapDataMapPointsToDomain)
            .flowOn(mapDispatcher)
    }

    override suspend fun fetchMapPoints(): List<DomainMapPoint> = withContext(dispatcherIo) {
        mapPointQueries.selectAll().executeAsList().map(::getDomainMapPoint)
    }

    override suspend fun saveMapPoint(
        name: String,
        latitude: Double,
        longitude: Double,
        profile: SimpleProfile?
    ) = withContext(dispatcherIo) {
        mapPointQueries.insert(
            name = name,
            profileName = profile?.name,
            latitude = latitude,
            longitude = longitude
        )
    }

    override suspend fun getMapPoint(id: Long): DomainMapPoint? = withContext(dispatcherIo) {
        mapPointQueries.select(id).executeAsOneOrNull()?.let(::getDomainMapPoint)
    }

    private fun mapDataMapPointsToDomain(dataMapPoints: List<DataMapPoint>) =
        dataMapPoints.map(::getDomainMapPoint)

    private fun getDomainMapPoint(dataMapPoint: DataMapPoint): DomainMapPoint =
        DomainMapPoint(
            id = dataMapPoint.id,
            name = dataMapPoint.name,
            latitude = dataMapPoint.latitude,
            longitude = dataMapPoint.longitude,
            profile = profileMapper.mapProfile(dataMapPoint.profileName),
        )

}