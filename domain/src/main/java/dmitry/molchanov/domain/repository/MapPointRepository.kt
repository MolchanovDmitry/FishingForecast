package dmitry.molchanov.domain.repository

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import kotlinx.coroutines.flow.Flow

interface MapPointRepository {

    fun fetchMapPointsFlow(): Flow<List<MapPoint>>

    suspend fun fetchMapPoints(): List<MapPoint>

    suspend fun saveMapPoint(name: String, latitude: Double, longitude: Double, profile: SimpleProfile?)

    suspend fun getMapPoint(id: Long): MapPoint?
}
