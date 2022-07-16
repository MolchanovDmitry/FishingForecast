package dmitry.molchanov.fishingforecast.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

val MapPoint.isCommonProfile: Boolean
    get() = profileName == null

fun MapPoint.string() = Json.encodeToString(this)

fun String.toMapPoint() = Json.decodeFromString<MapPoint>(this)