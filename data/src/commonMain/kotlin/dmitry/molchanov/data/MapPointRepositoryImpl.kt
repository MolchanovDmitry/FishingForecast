package dmitry.molchanov.data

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.MapPointRepository

class MapPointRepositoryImpl : MapPointRepository {

    private val list = mutableListOf<MapPoint>()

    override suspend fun fetchMapPoints(): List<MapPoint> {
        return list
    }

    override suspend fun saveMapPoint(profile: Profile, mapPoint: MapPoint) {
        list.add(mapPoint)
    }
}