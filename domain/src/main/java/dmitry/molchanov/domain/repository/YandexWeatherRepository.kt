package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.RawWeatherData

/**
 * Источник данных яндекс погоды.
 * TODO переименовать
 */
interface YandexWeatherRepository {

    /** Получить текущее значение погоды и прогноз на ближайшее время. */
    suspend fun getYandexWeatherDate(mapPoint: MapPoint): Result<List<RawWeatherData>>
}