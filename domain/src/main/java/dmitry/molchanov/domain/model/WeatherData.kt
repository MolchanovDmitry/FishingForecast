package dmitry.molchanov.domain.model

import dmitry.molchanov.domain.model.WindDir.C
import dmitry.molchanov.domain.model.WindDir.E
import dmitry.molchanov.domain.model.WindDir.N
import dmitry.molchanov.domain.model.WindDir.NE
import dmitry.molchanov.domain.model.WindDir.NW
import dmitry.molchanov.domain.model.WindDir.S
import dmitry.molchanov.domain.model.WindDir.SE
import dmitry.molchanov.domain.model.WindDir.SW
import dmitry.molchanov.domain.model.WindDir.W
import dmitry.molchanov.domain.utils.DayPart
import kotlinx.serialization.Serializable

/**
 * Данные погоды бизнес логики.
 *
 * @property date за какое время прогноз
 * @property mapPoint на какой точке прогноз
 * @property temperature температура
 * @property wind ветер
 * @property humidity влажность
 * @property moonCode фаза луны
 */
@Serializable
data class WeatherData(
    val id: Long,
    val date: WeatherDate,
    val mapPoint: MapPoint,
    val pressure: Pressure? = null,
    val temperature: Temperature? = null,
    val wind: Wind? = null,
    val moonCode: Int?,
    val humidity: Float? = null,
)

data class RawWeatherData(
    val date: Long,
    val mapPoint: MapPoint,
    val pressure: Pressure? = null,
    val temperature: Temperature? = null,
    val wind: Wind? = null,
    val humidity: Float? = null,
    val moonCode: Int?
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
    val dir: WindDir?,
)

@Serializable
class Pressure(
    val mm: Float?,
    val pa: Float?,
)

/**
 * Представление погодного времени
 *
 * @param roundedValue округленное значение до [DayPart]
 * @param dayPart часть дня [утро, день, вечер, ночь]
 */
@Serializable
data class WeatherDate(
    val roundedValue: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    val dayPart: DayPart
)

/**
 * Направление ветрка
 *
 * @property NW северо-западное
 * @property N северное
 * @property NE северо-восточное.
 * @property E восточное.
 * @property SE юго-восточное.
 * @property S южное.
 * @property SW юго-западное.
 * @property W западное.
 * @property C штиль.
 */
enum class WindDir(val value: String) {
    NW("nw"),
    N("n"),
    NE("ne"),
    E("e"),
    SE("se"),
    S("s"),
    SW("sw"),
    W("w"),
    C("c");

    companion object {
        fun getByValue(value: String): WindDir? =
            values().associateBy(WindDir::value)[value]
    }
}
