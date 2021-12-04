package dmitry.molchanov.data.yandexapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Info (
    @SerialName("url")
    val url: String,
    @SerialName("lat")
    val lat: Int,
    @SerialName("lon")
    val lon: Int,
)