package dmitry.molchanov.data.yandexapi.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Fact(
    @SerialName("obs_time")
    val obsTime: Int? = null,
    @SerialName("temp")
    val temp: Float,
    @SerialName("temp_water")
    val tempWater: Float? = null,
    @SerialName("feels_like")
    val feelsLike: Int? = null,
    @SerialName("icon")
    val icon: String,
    @SerialName("condition")
    val condition: String,
    @SerialName("wind_speed")
    val windSpeed: Float? = null,
    @SerialName("wind_dir")
    val windDir: String? = null,
    @SerialName("pressure_mm")
    val pressureMm: Float? = null,
    @SerialName("pressure_pa")
    val pressurePa: Float? = null,
    @SerialName("humidity")
    val humidity: Float,
    @SerialName("daytime")
    val daytime: String,
    @SerialName("polar")
    val polar: Boolean,
    @SerialName("season")
    val season: String,
    @SerialName("wind_gust")
    val windGust: Float? = null
)