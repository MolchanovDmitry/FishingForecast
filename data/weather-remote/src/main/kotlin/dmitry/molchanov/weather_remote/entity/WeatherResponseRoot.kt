package dmitry.molchanov.weather_remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponseRoot (
    @SerialName("now")
    val now: Int,
    @SerialName("now_dt")
    val nowDt: String? = null,
    @SerialName("info")
    val info: Info,
    @SerialName("fact")
    val fact: Fact,
    @SerialName("forecast")
    val forecast: Forecast,
)