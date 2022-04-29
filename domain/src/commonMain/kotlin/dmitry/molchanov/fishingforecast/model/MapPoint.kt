package dmitry.molchanov.fishingforecast.model

import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Точка на карте.
 */
@Serializable
data class MapPoint(
    val id: String,
    val name: String,
    val profileName: String?,
    val latitude: Double,
    val longitude: Double
)

fun MapPoint.string() = Json.encodeToString(this)

fun String.toMapPoint() = Json.decodeFromString<MapPoint>(this)