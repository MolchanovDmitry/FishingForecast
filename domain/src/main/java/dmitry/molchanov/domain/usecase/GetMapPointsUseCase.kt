package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.repository.MapPointRepository
import kotlinx.coroutines.flow.Flow

/**
 * Получить все точки на карте в профиле.
 */
class GetMapPointsUseCase(
    private val mapPointRepository: MapPointRepository
) {

    fun executeFlow(): Flow<List<MapPoint>> = mapPointRepository.fetchMapPointsFlow()

    suspend fun execute(): List<MapPoint> = mapPointRepository.fetchMapPoints()
}
