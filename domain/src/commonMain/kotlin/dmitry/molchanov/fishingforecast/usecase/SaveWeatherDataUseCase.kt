package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.repository.YandexWeatherRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Получить данные о погоде из яндекс репозитория и сохранить в базе
 */
class SaveWeatherDataUseCase(
    private val weatherDataRepository: WeatherDataRepository,
    private val yandexWeatherRepository: YandexWeatherRepository
) {

    suspend fun execute(mapPoint: MapPoint) = withContext(ioDispatcher) {
        yandexWeatherRepository.getYandexWeatherDate(mapPoint)
            .getOrNull()
            ?.ifEmpty { null }
            ?.let { weatherDataFromYandex -> weatherDataRepository.saveWeatherData(weatherDataFromYandex) }
    }
}