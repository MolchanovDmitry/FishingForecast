package dmitry.molchanov.db

import dmitry.molchanov.db.MapPoint as DataMapPoint
import dmitry.molchanov.domain.model.MapPoint as DomainMapPoint
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.mapper.ProfileMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MapPointRepositoryImpl(
    private val profileMapper: ProfileMapper,
    private val mapPointQueries: MapPointQueries,
) : MapPointRepository {

    override fun fetchMapPointsFlow(): Flow<List<DomainMapPoint>> {
        return mapPointQueries.selectAll()
            .asFlow()
            .mapToList()
            .map(::mapDataMapPointsToDomain)
    }

    override suspend fun fetchMapPoints(): List<DomainMapPoint> {
        return mapPointQueries.selectAll().executeAsList().map(::getDomainMapPoint)
    }

    override suspend fun saveMapPoint(
        name: String,
        latitude: Double,
        longitude: Double,
        profile: SimpleProfile?
    ) {
        mapPointQueries.insert(name = name, profileName = profile?.name, latitude = latitude, longitude = longitude)
    }

    override suspend fun getMapPoint(id: Long): DomainMapPoint? {
        return mapPointQueries.select(id).executeAsOneOrNull()?.let(::getDomainMapPoint)
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