package dmitry.molchanov.fishingforecast.model

import dmitry.molchanov.domain.model.ForecastSettingsItem

/**
 * Результат прогноза.
 *
 * @property forecastSettingsItem элемент прогнозирования.
 * @property isGood хороший показатель?
 */
class Forecast(
    val forecastSettingsItem: ForecastSettingsItem,
    val isGood: Boolean
)
