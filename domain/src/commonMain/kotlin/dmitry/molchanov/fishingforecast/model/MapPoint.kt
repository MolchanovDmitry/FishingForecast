package dmitry.molchanov.fishingforecast.model

import kotlinx.serialization.Serializable

/**
 * Точка на карте.
 */
@Serializable
data class MapPoint(
    val id: Long,
    val name: String,
    val profile: Profile,
    val latitude: Double,
    val longitude: Double,
)