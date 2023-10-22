package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.repository.MapPointRepository

class GetMapPointByIdUseCase(
    private val mapPointRepository: MapPointRepository,
) {

    suspend fun execute(mapPointId: Long): MapPoint? {
        return mapPointRepository.getMapPoint(mapPointId)
    }
}
