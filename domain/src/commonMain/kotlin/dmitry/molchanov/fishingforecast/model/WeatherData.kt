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
    val pressure: Pressure,
    val temperature: Temperature,
    val temperatureWater: Float,
    val wind: Wind,
    val humidity: Float

)

class Temperature(
    val min: Float,
    val avg: Float,
    val max: Float,
)

class Wind(
    val speed: Float,
    val gust: Float,
    val dir: String
)

class Pressure(
    val mm: Float,
    val pa: Float
)

