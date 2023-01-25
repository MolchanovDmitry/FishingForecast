package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.repository.MapPointRepository
import kotlinx.coroutines.withContext

class GetMapPointByIdUseCase(private val mapPointRepository: MapPointRepository) {

    suspend fun execute(mapPointId: Long) = withContext(ioDispatcher) {
        mapPointRepository.getMapPoint(mapPointId)
    }
}
