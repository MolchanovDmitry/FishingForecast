package dmitry.molchanov.fishingforecast.model

import dmitry.molchanov.fishingforecast.utils.string
import kotlin.random.Random

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
    val temperatureWater: Float? = null,
    val wind: Wind? = null,
    val humidity: Float? = null

)

class Temperature(
    val min: Float? = null,
    val avg: Float? = null,
    val max: Float? = null,
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

val data = listOf(
    1640995200000,
    1641081600000,
    1641168000000,
    1641254400000,
    1641340800000,
    1641427200000,
    1641513600000
).also {
    it.forEach {
        println("1488 " + it.string())
    }
}
val mockMapPoint = MapPoint("some point", profileName = "", 0.0, 0.0)
val mockMapPoint2 = MapPoint("some point 2", profileName = "", 100.0, 100.0)
val mockWeatherData by lazy {
    data.map {
        WeatherData(
            date = it,
            mapPoint = mockMapPoint,
            temperature = Temperature(
                avg = 10F + Random.nextFloat() * (25F - 10F)
            )
        )
    }
}

