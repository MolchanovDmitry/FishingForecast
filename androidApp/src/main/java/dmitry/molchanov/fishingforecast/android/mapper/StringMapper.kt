package dmitry.molchanov.fishingforecast.android.mapper

import android.content.Context
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.model.ForecastSettingsItem
import dmitry.molchanov.fishingforecast.model.ForecastSettingsItem.*

fun ForecastSettingsItem.toString(context: Context): String =
    when (this) {
        OBSERVATION_PERIOD -> R.string.observation_period
        PRESSURE_MM -> R.string.pressure_mm
        PRESSURE_PA -> R.string.pressure_pa
        WIND_SPEED -> R.string.wind_speed
        WIND_GUST -> R.string.wind_gust
        TEMPERATURE_MIN -> R.string.temperature_min
        TEMPERATURE_AVG -> R.string.temperature_avg
        TEMPERATURE_MAX -> R.string.temperature_max
        TEMPERATURE_WATER -> R.string.temperature_water
        HUMIDITY -> R.string.humidity
    }.let(context::getString)