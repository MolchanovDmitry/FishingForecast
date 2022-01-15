package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.utils.TimeMs
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Получить сохраненное значение погоды по точке на карте.
 */
class GetSavedWeatherDataUseCase(private val weatherDataRepository: WeatherDataRepository) {

    suspend fun execute(mapPoint: MapPoint, from: TimeMs, to: TimeMs): List<WeatherData> =
        withContext(ioDispatcher) {
            weatherDataRepository.fetchWeatherData(mapPoint, from, to)
        }
}