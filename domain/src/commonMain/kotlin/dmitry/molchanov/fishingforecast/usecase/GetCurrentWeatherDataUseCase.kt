package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.YandexWeatherRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Получить сохраненное значение погоды по точке на карте.
 */
class GetCurrentWeatherDataUseCase(
    private val yandexWeatherRepository: YandexWeatherRepository
) {

    suspend fun execute(mapPoint: MapPoint): List<WeatherData>? = withContext(ioDispatcher) {
        yandexWeatherRepository.getYandexWeatherDate(mapPoint).getOrNull()
    }
}