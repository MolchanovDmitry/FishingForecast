package dmitry.molchanov.weather_remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Part(
    @SerialName("part_name")
    val partName: String? = null,
    @SerialName("temp_min")
    val tempMin: Float? = null,
    @SerialName("temp_avg")
    val tempAvg: Float? = null,
    @SerialName("temp_max")
    val tempMax: Float? = null,
    @SerialName("wind_speed")
    val windSpeed: Float? = null,
    @SerialName("wind_gust")
    val windGust: Float? = null,
    @SerialName("wind_dir")
    val windDir: String? = null,
    @SerialName("pressure_mm")
    val pressureMm: Float? = null,
    @SerialName("pressure_pa")
    val pressurePa: Float? = null,
    @SerialName("humidity")
    val humidity: Float,
    @SerialName("prec_mm")
    val precMm: Double? = null,
    @SerialName("prec_prob")
    val precProb: Double? = null,
    @SerialName("prec_period")
    val precPeriod: Double? = null,
    @SerialName("icon")
    val icon: String,
    @SerialName("condition")
    val condition: String,
    @SerialName("feels_like")
    val feelsLike: Int? = null,
    @SerialName("daytime")
    val daytime: String,
    @SerialName("polar")
    val polar: Boolean,
)