package dmitry.molchanov.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.MapPointQueries
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dmitry.molchanov.db.MapPoint as DataMapPoint

class MapPointRepositoryImpl(private val mapPointQueries: MapPointQueries) : MapPointRepository {

    override fun fetchMapPoints(): Flow<List<MapPoint>> {
        return mapPointQueries.selectAll()
            .asFlow()
            .mapToList()
            .map(::mapDataMapPointsToDomain)
    }

    override suspend fun saveMapPoint(mapPoint: MapPoint) {
        mapPointQueries.insert(mapPoint.mapToData())
    }

    private fun mapDataMapPointsToDomain(dataMapPoints: List<DataMapPoint>) =
        dataMapPoints.map {
            MapPoint(
                id = it.id,
                name = it.name,
                profileName = it.profileName,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }

    private fun MapPoint.mapToData() =
        DataMapPoint(
            id = this.id,
            name = this.name,
            profileName = this.profileName,
            latitude = this.latitude,
            longitude = this.longitude
        )

}