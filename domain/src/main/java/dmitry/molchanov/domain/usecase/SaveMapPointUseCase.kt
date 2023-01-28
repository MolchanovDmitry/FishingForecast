package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.MapPointRepository
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
