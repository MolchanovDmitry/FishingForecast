package dmitry.molchanov.fishingforecast.model

import kotlinx.serialization.Serializable

@Serializable
class Result(
    val id: Long,
    val name: String,
    val mapPoint: MapPoint,
)