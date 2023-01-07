package dmitry.molchanov.domain.model

import kotlinx.serialization.Serializable

/**
 * Данные погоды бизнес логики.
 *
 * @property date за какое время прогноз
 * @property mapPoint на какой точке прогноз
 * @property temperature температура
 * @property wind ветер
 * @property humidity влажность
 */
@Serializable
data class WeatherData(
    val id: Long,
    val date: Long,
    val mapPoint: MapPoint,
    val pressure: Pressure? = null,
    val temperature: Temperature? = null,
    val wind: Wind? = null,
    val humidity: Float? = null,
)

data class RawWeatherData(
    val date: Long,
    val mapPoint: MapPoint,
    val pressure: Pressure? = null,
    val temperature: Temperature? = null,
    val wind: Wind? = null,
    val humidity: Float? = null,
)

@Serializable
class Temperature(
    val min: Float? = null,
    val avg: Float? = null,
    val max: Float? = null,
    val water: Float? = null,
)

@Serializable
class Wind(
    val speed: Float?,
    val gust: Float?,
    val dir: String?,
)

@Serializable
class Pressure(
    val mm: Float?,
    val pa: Float?,
)
