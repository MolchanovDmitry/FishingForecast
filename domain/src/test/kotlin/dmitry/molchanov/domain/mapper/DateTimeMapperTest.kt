package dmitry.molchanov.domain.mapper

import dmitry.molchanov.domain.utils.DayPart
import dmitry.molchanov.domain.utils.getHour
import java.text.SimpleDateFormat
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Проверяем парсинг времени
 */
class DateTimeMapperTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Test
    fun test24() {
        val rawHourToDayPart = mapOf(
            24 to DayPart.NIGHT,
            0 to DayPart.NIGHT,
            1 to DayPart.NIGHT,
            2 to DayPart.NIGHT,
            3 to DayPart.NIGHT,
            4 to DayPart.NIGHT,
            5 to DayPart.NIGHT,
            6 to DayPart.MORNING,
            7 to DayPart.MORNING,
            8 to DayPart.MORNING,
            9 to DayPart.MORNING,
            10 to DayPart.MORNING,
            11 to DayPart.MORNING,
            12 to DayPart.MIDDAY,
            13 to DayPart.MIDDAY,
            14 to DayPart.MIDDAY,
            15 to DayPart.MIDDAY,
            16 to DayPart.MIDDAY,
            17 to DayPart.MIDDAY,
            18 to DayPart.EVENING,
            19 to DayPart.EVENING,
            20 to DayPart.EVENING,
            21 to DayPart.EVENING,
            22 to DayPart.EVENING,
            23 to DayPart.EVENING,
        )

        rawHourToDayPart.forEach { (hour, dayPart) ->
            val rawDate = dateFormat.parse("1945-05-09T${hour}:00:00Z").time
            val parsedHour = rawDate.getHour()
            val parsedDayPart = rawDate.toWeatherDate().dayPart
            assertEquals(
                "hour: $hour, " +
                        "parsedHour: $parsedHour, " +
                        "expected day part: ${dayPart.name}, " +
                        "parsed day part: ${parsedDayPart.name}",
                dayPart,
                parsedDayPart
            )
        }
    }
}