package dmitry.molchanov.fishingforecast.utils

import kotlinx.datetime.*

typealias TimeMs = Long

const val SEC_IN_DAY = 24 * 60 * 60

val nowUnixTime: TimeMs
    get() = Clock.System.now().epochSeconds

val TimeMs.nightTime: TimeMs
    get() = getTimeRoundedByDayPart(DayPart.NIGHT)

val TimeMs.morningTime: TimeMs
    get() = getTimeRoundedByDayPart(DayPart.MORNING)

val TimeMs.midDayTime: TimeMs
    get() = getTimeRoundedByDayPart(DayPart.MIDDAY)

val TimeMs.eveningTime: TimeMs
    get() = getTimeRoundedByDayPart(DayPart.EVENING)

val TimeMs.daysCount: Int
    get() = Instant.fromEpochMilliseconds((this))
        .epochSeconds.getDayCount()

/**
 * Получить время минус [count] дней.
 */
fun TimeMs.getDayBefore(count: Int): TimeMs {
    val daysBeforeSec = count * SEC_IN_DAY
    return Instant.fromEpochMilliseconds((this - daysBeforeSec)).epochSeconds
}

fun TimeMs.getDayCount(): Int {
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInUtc.dayOfMonth
}

/**
 * Получить время округленное значением [dayPart].
 */
private fun TimeMs.getTimeRoundedByDayPart(dayPart: DayPart): TimeMs {
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    val roundedTime = LocalDateTime(
        year = datetimeInUtc.year,
        monthNumber = datetimeInUtc.monthNumber,
        dayOfMonth = datetimeInUtc.dayOfMonth,
        dayPart.hour,
        0,
        0,
        0
    )
    return roundedTime.toInstant(TimeZone.UTC).epochSeconds
}

/**
 * Распечатать время.
 */
fun TimeMs.string(): String {
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    return "Date: ${datetimeInUtc.year}" +
            ".${datetimeInUtc.monthNumber}" +
            ".${datetimeInUtc.dayOfMonth}" +
            ".${datetimeInUtc.hour}" +
            ":${datetimeInUtc.minute}"
}

/**
 * Время суток.
 * @property hour час времени суток.
 */
private enum class DayPart(val hour: Int) {
    NIGHT(0),
    MORNING(6),
    MIDDAY(12),
    EVENING(18)
}
