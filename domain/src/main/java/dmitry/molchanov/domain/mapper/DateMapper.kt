package dmitry.molchanov.domain.mapper

import dmitry.molchanov.domain.model.WeatherDate
import dmitry.molchanov.domain.utils.dayPart
import dmitry.molchanov.domain.utils.getDayCount
import dmitry.molchanov.domain.utils.getMonthCount
import dmitry.molchanov.domain.utils.getYearCount
import java.util.*


/** Мапит [long] представление в модель [WeatherDate] */

fun Long.toWeatherDate(): WeatherDate {
    val year = this.getYearCount()
    val month = this.getMonthCount()
    val day = this.getDayCount()
    val dayPart = this.dayPart

    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month - 1)
    cal.set(Calendar.DAY_OF_MONTH, day)
    cal.set(Calendar.HOUR_OF_DAY, dayPart.hour)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return WeatherDate(
        roundedValue = cal.timeInMillis,
        year = year,
        month = month,
        day = day,
        dayPart = dayPart
    )
}
