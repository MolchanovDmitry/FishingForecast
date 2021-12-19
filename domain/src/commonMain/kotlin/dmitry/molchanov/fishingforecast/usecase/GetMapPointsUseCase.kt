package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Получить все точки на карте в профиле.
 */
class GetMapPointsUseCase(private val mapPointRepository: MapPointRepository) {

    fun execute(): Flow<List<MapPoint>> = mapPointRepository.fetchMapPoints().flowOn(ioDispatcher)
}