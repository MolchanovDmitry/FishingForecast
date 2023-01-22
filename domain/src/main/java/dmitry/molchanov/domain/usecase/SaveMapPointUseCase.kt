package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Сохраняет точку на карте.
 */
class SaveMapPointUseCase(private val repository: MapPointRepository) {

    suspend fun execute(
        pointName: String,
        profile: Profile,
        latitude: Double,
        longitude: Double
    ) = withContext(ioDispatcher) {
        repository.saveMapPoint(
            name = pointName,
            latitude = latitude,
            longitude = longitude,
            profile = profile as? SimpleProfile,
        )
    }
}
