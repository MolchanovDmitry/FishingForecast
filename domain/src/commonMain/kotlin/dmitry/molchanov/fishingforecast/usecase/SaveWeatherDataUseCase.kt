package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Сохранить данные погоды.
 */
class SaveWeatherDataUseCase(private val repository: WeatherDataRepository) {

    suspend fun execute(weatherData: List<WeatherData>) = withContext(ioDispatcher) {
        repository.saveWeatherData(weatherData)
    }
}