package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import kotlinx.coroutines.flow.Flow

interface MapPointRepository {

    fun fetchMapPoints(): Flow<List<MapPoint>>

    suspend fun saveMapPoint(mapPoint: MapPoint)
}