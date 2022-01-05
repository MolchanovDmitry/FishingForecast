package dmitry.molchanov.fishingforecast.android.ui.setting

import dmitry.molchanov.fishingforecast.model.*

fun ForecastMark.toString() = when (this) {
    is MinValueForecastMark ->
        "min:"
    is MaxValueForecastMark ->
        "max:"
    is DeltaForecastMark ->
        "delta:"
    is ExactValueForecastMark ->
        "значение:"
}
