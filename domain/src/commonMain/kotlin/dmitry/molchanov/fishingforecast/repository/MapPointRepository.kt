package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import kotlinx.coroutines.flow.Flow

interface MapPointRepository {

    fun fetchMapPointsFlow(): Flow<List<MapPoint>>

    suspend fun fetchMapPoints(): List<MapPoint>

    suspend fun saveMapPoint(mapPoint: MapPoint)

    suspend fun getMapPoint(id: String): MapPoint?
}