package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.WeatherData

/**
 * Источник данных яндекс погоды.
 */
interface YandexWeatherRepository {

    /** Получить текущее значение погоды и прогноз на ближайшее время. */
    suspend fun getCurrentWeatherWithForecast(mapPoint: MapPoint): List<WeatherData>
}