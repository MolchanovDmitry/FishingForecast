package dmitry.molchanov.fishingforecast.utils

import kotlinx.datetime.*

typealias UnixTime = Long

const val SEC_IN_DAY = 24 * 60 * 60
private const val ONE_SEC = 1_000L

val nowUnixTime: UnixTime
    get() = Clock.System.now().epochSeconds

val UnixTime.nightTime: UnixTime
    get() = getTimeRoundedByDayPart(DayPart.NIGHT)

val UnixTime.morningTime: UnixTime
    get() = getTimeRoundedByDayPart(DayPart.MORNING)

val UnixTime.midDayTime: UnixTime
    get() = getTimeRoundedByDayPart(DayPart.MIDDAY)

val UnixTime.eveningTime: UnixTime
    get() = getTimeRoundedByDayPart(DayPart.EVENING)

val UnixTime.daysCount: Int
    get() = Instant.fromEpochMilliseconds((this) * ONE_SEC)
        .epochSeconds.getDayCount()

/**
 * Получить время минус [count] дней.
 */
fun UnixTime.getDayBefore(count: Int): UnixTime {
    val daysBeforeSec = count * SEC_IN_DAY
    return Instant.fromEpochMilliseconds((this - daysBeforeSec) * ONE_SEC).epochSeconds
}

private fun UnixTime.getDayCount(): Int {
    val nowMilliSec = Instant.fromEpochMilliseconds(this * ONE_SEC)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInUtc.dayOfMonth
}

/**
 * Получить время округленное значением [dayPart].
 */
private fun UnixTime.getTimeRoundedByDayPart(dayPart: DayPart): UnixTime {
    val nowMilliSec = Instant.fromEpochMilliseconds(this * ONE_SEC)
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
fun UnixTime.string(): String {
    val nowMilliSec = Instant.fromEpochMilliseconds(this * ONE_SEC)
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
