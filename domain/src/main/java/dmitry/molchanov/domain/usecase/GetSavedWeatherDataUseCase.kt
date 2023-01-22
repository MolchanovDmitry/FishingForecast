package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.WeatherDataRepository
import dmitry.molchanov.domain.utils.TimeMs
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Получить сохраненное значение погоды по точке на карте.
 */
class GetSavedWeatherDataUseCase(private val weatherDataRepository: WeatherDataRepository) {

    suspend fun executeFlow(mapPoint: MapPoint, from: TimeMs, to: TimeMs): Flow<List<WeatherData>> =
        withContext(ioDispatcher) {
            weatherDataRepository.fetchWeatherDataFlow(mapPoint, from, to)
        }

    suspend fun execute(mapPoint: MapPoint, from: TimeMs, to: TimeMs): List<WeatherData> =
        withContext(ioDispatcher) {
            weatherDataRepository.fetchWeatherData(mapPoint, from, to)
        }
}