package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.repository.YandexWeatherRepository

/**
 * Получить сохраненное значение погоды по точке на карте.
 */
class GetCurrentWeatherDataUseCase (private val yandexWeatherRepository: YandexWeatherRepository) {

    /*suspend fun execute(mapPoint: MapPoint): List<WeatherData> = withContext(ioDispatcher) {
        yandexWeatherRepository.getYandexWeatherDate(mapPoint).getOrNull()!! as List<WeatherData>
    }*/
}