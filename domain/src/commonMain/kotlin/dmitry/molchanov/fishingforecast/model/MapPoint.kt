package dmitry.molchanov.fishingforecast.model

/**
 * Точка на карте.
 */
data class MapPoint(
    val name: String,
    val profileName: String?,
    val latitude: Double,
    val longitude: Double
)