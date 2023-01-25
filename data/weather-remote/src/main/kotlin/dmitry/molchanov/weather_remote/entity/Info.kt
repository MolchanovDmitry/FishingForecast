package dmitry.molchanov.weather_remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Info (
    @SerialName("url")
    val url: String,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lon")
    val lon: Double,
)