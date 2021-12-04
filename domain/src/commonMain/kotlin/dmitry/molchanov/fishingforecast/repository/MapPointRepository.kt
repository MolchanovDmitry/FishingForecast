package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile

interface MapPointRepository {

    suspend fun fetchMapPoints(): List<MapPoint>

    suspend fun saveMapPoint(profile: Profile, mapPoint: MapPoint)
}