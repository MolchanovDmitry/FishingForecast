package dmitry.molchanov.weather_remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Part(
    @SerialName("part_name")
    val partName: String? = null,
    @SerialName("temp_min")
    val tempMin: Int? = null,
    @SerialName("temp_avg")
    val tempAvg: Int? = null,
    @SerialName("temp_max")
    val tempMax: Int? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null,
    @SerialName("wind_gust")
    val windGust: Double? = null,
    @SerialName("wind_dir")
    val windDir: String? = null,
    @SerialName("pressure_mm")
    val pressureMm: Double? = null,
    @SerialName("pressure_pa")
    val pressurePa: Double? = null,
    @SerialName("humidity")
    val humidity: Int,
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