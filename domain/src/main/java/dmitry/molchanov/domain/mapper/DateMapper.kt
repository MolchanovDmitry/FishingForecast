package dmitry.molchanov.domain.mapper

import dmitry.molchanov.domain.model.WeatherDate
import dmitry.molchanov.domain.utils.dayPart
import dmitry.molchanov.domain.utils.getDayCount
import dmitry.molchanov.domain.utils.getMonthCount
import dmitry.molchanov.domain.utils.getYearCount

/** Мапит [long] представление в модель [WeatherDate] */

fun Long.toWeatherDate(): WeatherDate =
    WeatherDate(
        raw = this,
        year = this.getYearCount(),
        month = this.getMonthCount(),
        day = this.getDayCount(),
        dayPart = this.dayPart
    )