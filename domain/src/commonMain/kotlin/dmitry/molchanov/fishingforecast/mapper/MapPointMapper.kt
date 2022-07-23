package dmitry.molchanov.fishingforecast.mapper

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.usecase.GetMapPointByIdUseCase

class MapPointMapper(private val getMapPointsByIdUseCase: GetMapPointByIdUseCase) {

    suspend fun getMapPointById(id: Long): MapPoint? {
        return getMapPointsByIdUseCase.execute(id)
    }
}