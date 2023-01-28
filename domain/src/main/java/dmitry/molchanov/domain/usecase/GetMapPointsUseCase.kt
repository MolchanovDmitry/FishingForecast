package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.repository.MapPointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Получить все точки на карте в профиле.
 */
class GetMapPointsUseCase(private val mapPointRepository: MapPointRepository) {

    fun executeFlow(): Flow<List<MapPoint>> =
        mapPointRepository.fetchMapPointsFlow()
            .flowOn(ioDispatcher)

    suspend fun execute(): List<MapPoint> = withContext(ioDispatcher) {
        mapPointRepository.fetchMapPoints()
    }
}
