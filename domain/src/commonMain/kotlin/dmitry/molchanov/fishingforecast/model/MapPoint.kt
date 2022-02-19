package dmitry.molchanov.fishingforecast.model

/**
 * Точка на карте.
 */
data class MapPoint(
    val id: String,
    val name: String,
    val profileName: String?,
    val latitude: Double,
    val longitude: Double
)