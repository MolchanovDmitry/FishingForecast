package dmitry.molchanov.domain.model

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
