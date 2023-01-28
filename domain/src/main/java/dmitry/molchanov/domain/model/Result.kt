package dmitry.molchanov.domain.model

import kotlinx.serialization.Serializable

@Serializable
class Result(
    val id: Long,
    val name: String,
    val mapPoint: MapPoint,
)
