package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.repository.WeatherDataRepository
import dmitry.molchanov.domain.repository.YandexWeatherRepository
import kotlinx.coroutines.withContext

/**
 * Получить данные о погоде из яндекс репозитория и сохранить в базе
 */
class SaveWeatherDataUseCase(
    private val weatherDataRepository: WeatherDataRepository,
    private val yandexWeatherRepository: YandexWeatherRepository
) {

    suspend fun execute(mapPoint: MapPoint): Result<Unit> = withContext(ioDispatcher) {
        yandexWeatherRepository.getYandexWeatherDate(mapPoint)
            .map { weatherDataFromYandex ->
                weatherDataRepository.saveRawWeatherData(weatherDataFromYandex)
            }
    }
}
