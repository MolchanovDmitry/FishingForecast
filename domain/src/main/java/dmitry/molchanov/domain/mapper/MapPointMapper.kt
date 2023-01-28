package dmitry.molchanov.domain.mapper

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.usecase.GetMapPointByIdUseCase

class MapPointMapper(private val getMapPointsByIdUseCase: GetMapPointByIdUseCase) {

    suspend fun getMapPointById(id: Long): MapPoint? {
        return getMapPointsByIdUseCase.execute(id)
    }
}
