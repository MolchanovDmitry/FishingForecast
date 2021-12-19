package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import kotlinx.coroutines.flow.Flow

interface MapPointRepository {

    fun fetchMapPoints(): Flow<List<MapPoint>>

    suspend fun saveMapPoint(profile: Profile, mapPoint: MapPoint)
}