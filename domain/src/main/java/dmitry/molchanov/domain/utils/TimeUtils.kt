package dmitry.molchanov.domain.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

typealias TimeMs = Long

const val SEC_IN_DAY = 24 * 60 * 60

const val ONE_SEC = 1_000L

const val ONE_HOUR = 60 * 60 * ONE_SEC

const val ONE_DAY = SEC_IN_DAY * ONE_SEC

/*val nowUnixTime: TimeMs
    get() = Clock.System.now().toEpochMilliseconds()*/

val TimeMs.nightTime: TimeMs
    get() = getTimeRoundedByDayPart(DayPart.NIGHT)

val TimeMs.morningTime: TimeMs
    get() = getTimeRoundedByDayPart(DayPart.MORNING)

val TimeMs.midDayTime: TimeMs
    get() = getTimeRoundedByDayPart(DayPart.MIDDAY)

val TimeMs.eveningTime: TimeMs
    get() = getTimeRoundedByDayPart(DayPart.EVENING)

val TimeMs.daysCount: Int
    get() = Instant.fromEpochMilliseconds((this)).epochSeconds.getDayCount()

val TimeMs.dayPart: DayPart
    get() = when (this.getHour()) {
        in 6..11 -> DayPart.MORNING
        in 12..17 -> DayPart.MIDDAY
        in 18..23 -> DayPart.EVENING
        else -> DayPart.NIGHT
    }

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

fun TimeMs.getMonthCount(): Int {
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInUtc.monthNumber
}

fun TimeMs.getYearCount(): Int {
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInUtc.year
}

fun TimeMs.getHour(): Int {
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    return datetimeInUtc.hour
}

fun TimeMs.getMonthDayCount(): Float {
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    val day = datetimeInUtc.dayOfMonth
    val month = datetimeInUtc.monthNumber
    return month + day / 100F
}

/**
 * Получить время округленное значением [dayPart].
 */
private fun TimeMs.getTimeRoundedByDayPart(dayPart: DayPart): TimeMs {
    val timeZone = TimeZone.currentSystemDefault()
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(timeZone)
    val roundedTime = LocalDateTime(
        year = datetimeInUtc.year,
        monthNumber = datetimeInUtc.monthNumber,
        dayOfMonth = datetimeInUtc.dayOfMonth,
        hour = dayPart.hour,
        0,
        0,
        0
    )
    return roundedTime.toInstant(timeZone).toEpochMilliseconds()
}

/**
 * Распечатать время.
 */
fun TimeMs.string(): String {
    val nowMilliSec = Instant.fromEpochMilliseconds(this)
    val datetimeInUtc: LocalDateTime = nowMilliSec.toLocalDateTime(TimeZone.currentSystemDefault())
    return " ${datetimeInUtc.year}" + ".${datetimeInUtc.monthNumber}" + ".${datetimeInUtc.dayOfMonth}" + ".${datetimeInUtc.hour}" + ":${datetimeInUtc.minute}"
}

/**
 * Время суток.
 * @property hour час времени суток.
 */
enum class DayPart(val hour: Int) {
    NIGHT(0), MORNING(6), MIDDAY(12), EVENING(18)
}

