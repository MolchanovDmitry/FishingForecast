package dmitry.molchanov.data.yandexapi.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forecast (
    @SerialName("date")
    val date: String,
    @SerialName("date_ts")
    val dateTs: Int? = null,
    @SerialName("week")
    val week: Int,
    @SerialName("sunrise")
    val sunrise: String? = null,
    @SerialName("sunset")
    val sunset: String? = null,
    @SerialName("moon_code")
    val dmoonCode: Int? = null,
    @SerialName("moon_text")
    val moonText: String? = null,
    @SerialName("parts")
    val parts: List<Part>,
)