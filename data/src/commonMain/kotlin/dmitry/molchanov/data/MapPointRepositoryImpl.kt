package dmitry.molchanov.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.MapPointQueries
import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dmitry.molchanov.db.MapPoint as DataMapPoint
import dmitry.molchanov.fishingforecast.model.MapPoint as DomainMapPoint

class MapPointRepositoryImpl(private val mapPointQueries: MapPointQueries) : MapPointRepository {

    override fun fetchMapPointsFlow(): Flow<List<DomainMapPoint>> {
        return mapPointQueries.selectAll()
            .asFlow()
            .mapToList()
            .map(::mapDataMapPointsToDomain)
    }

    override suspend fun fetchMapPoints(): List<DomainMapPoint> {
        return mapPointQueries.selectAll().executeAsList().map (::getDomainMapPoint)
    }

    override suspend fun saveMapPoint(mapPoint: DomainMapPoint) {
        mapPointQueries.insert(mapPoint.mapToData())
    }

    override suspend fun getMapPoint(id: String): DomainMapPoint? {
        return mapPointQueries.select(id).executeAsOneOrNull()?.let(::getDomainMapPoint)
    }

    private fun mapDataMapPointsToDomain(dataMapPoints: List<DataMapPoint>) =
        dataMapPoints.map(::getDomainMapPoint)

    private fun getDomainMapPoint(dataMapPoint: DataMapPoint): DomainMapPoint =
        DomainMapPoint(
            id = dataMapPoint.id,
            name = dataMapPoint.name,
            profileName = dataMapPoint.profileName,
            latitude = dataMapPoint.latitude,
            longitude = dataMapPoint.longitude
        )

    private fun DomainMapPoint.mapToData() =
        DataMapPoint(
            id = this.id,
            name = this.name,
            profileName = this.profileName,
            latitude = this.latitude,
            longitude = this.longitude
        )

}