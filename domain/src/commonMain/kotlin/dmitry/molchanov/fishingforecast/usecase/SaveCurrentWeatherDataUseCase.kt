package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

class SaveCurrentWeatherDataUseCase(private val weatherDataRepository: WeatherDataRepository) {

    suspend fun execute(weatherData: List<WeatherData>) = withContext(ioDispatcher) {
        weatherDataRepository.saveWeatherData(weatherData)
    }
}