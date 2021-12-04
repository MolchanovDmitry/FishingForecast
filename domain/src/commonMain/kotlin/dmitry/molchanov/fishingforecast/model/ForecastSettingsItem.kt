package dmitry.molchanov.fishingforecast.model

/**
 * Элемент прогнозирования.
 *
 * @property OBSERVATION_PERIOD период наблюдения в днях.
 * @property PRESSURE_MM настройка давления в миллиметрах ртутного столба.
 * @property PRESSURE_PA настройка давления в паскалях.
 * @property WIND_SPEED настройка скорости ветра.
 * @property WIND_GUST настройка порыва ветра.
 * @property TEMPERATURE_MIN настройка минимального температуры окружающей среды.
 * @property TEMPERATURE_AVG настройка средней температуры окружающей среды.
 * @property TEMPERATURE_MAX настройка максимальной температуры окружающей среды.
 * @property TEMPERATURE_WATER настройка температуры воды.
 * @property HUMIDITY настройка влажности.
 */
enum class ForecastSettingsItem {
    OBSERVATION_PERIOD,
    PRESSURE_MM,
    PRESSURE_PA,
    WIND_SPEED,
    WIND_GUST,
    TEMPERATURE_MIN,
    TEMPERATURE_AVG,
    TEMPERATURE_MAX,
    TEMPERATURE_WATER,
    HUMIDITY
}

/** Базовый класс вида показателей прогнозирования. */
sealed class ForecastMark

/** Допустимое минимальное значение для [ForecastSettingsItem]. */
class MinValueForecastMark(val value: Float) : ForecastMark()

/** Допустимое максимальное значение для [ForecastSettingsItem]. */
class MaxValueForecastMark(val value: Float) : ForecastMark()

/** Допустимое изменение(дельта) для [ForecastSettingsItem]. */
class DeltaForecastMark(val value: Float) : ForecastMark()

/** Точное значение для [ForecastSettingsItem]. */
class ExactValueForecastMark(val value: Float) : ForecastMark()

/**
 * Настройка прогнозирования.
 *
 * @property forecastSettingsItem элемент прогнозирования.
 * @property forecastMark значение.
 */
class ForecastSetting(
    val forecastSettingsItem: ForecastSettingsItem,
    val forecastMark: ForecastMark
)