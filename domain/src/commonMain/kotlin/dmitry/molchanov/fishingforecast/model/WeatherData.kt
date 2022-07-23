package dmitry.molchanov.fishingforecast.model

/**
 * Данные погоды бизнес логики.
 *
 * @property date за какое время прогноз
 * @property mapPoint на какой точке прогноз
 * @property temperature температура
 * @property wind ветер
 * @property humidity влажность
 */
class WeatherData(
    val date: Long,
    val mapPoint: MapPoint,
    val pressure: Pressure? = null,
    val temperature: Temperature? = null,
    val wind: Wind? = null,
    val humidity: Float? = null

)

class Temperature(
    val min: Float? = null,
    val avg: Float? = null,
    val max: Float? = null,
    val water: Float? = null
)

class Wind(
    val speed: Float?,
    val gust: Float?,
    val dir: String?
)

class Pressure(
    val mm: Float?,
    val pa: Float?
)