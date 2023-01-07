package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.domain.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

class GetMapPointByIdUseCase(private val mapPointRepository: MapPointRepository) {

    suspend fun execute(mapPointId: Long) = withContext(ioDispatcher) {
        mapPointRepository.getMapPoint(mapPointId)
    }

}