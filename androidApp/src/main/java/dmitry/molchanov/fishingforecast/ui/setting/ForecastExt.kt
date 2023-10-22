package dmitry.molchanov.fishingforecast.ui.setting

import dmitry.molchanov.domain.model.DeltaForecastMark
import dmitry.molchanov.domain.model.ExactValueForecastMark
import dmitry.molchanov.domain.model.ForecastMark
import dmitry.molchanov.domain.model.MaxValueForecastMark
import dmitry.molchanov.domain.model.MinValueForecastMark

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
