package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.utils.generateUuid
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Сохраняет точку на карте.
 */
class SaveMapPointUseCase(private val repository: MapPointRepository) {

    suspend fun execute(
        pointName: String,
        profile: SimpleProfile?,
        latitude: Double,
        longitude: Double
    ) = withContext(ioDispatcher) {
        repository.saveMapPoint(
            MapPoint(
                id = generateUuid(),
                name = pointName,
                latitude = latitude,
                longitude = longitude,
                profileName = profile?.name,
            )
        )
    }
}