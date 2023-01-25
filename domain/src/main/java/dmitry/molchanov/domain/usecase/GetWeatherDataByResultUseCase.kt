package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.ResultDataRepository
import dmitry.molchanov.domain.repository.WeatherDataRepository
import kotlinx.coroutines.withContext

class GetWeatherDataByResultUseCase(
    private val resultDataRepository: ResultDataRepository,
    private val weatherDataRepository: WeatherDataRepository,
) {

    suspend fun execute(result: Result): List<WeatherData> = withContext(ioDispatcher) {
        val weatherDataIds = resultDataRepository.getWeatherDataIdsByResult(result)
        val weatherData = weatherDataRepository.getWeatherDataByIds(weatherDataIds)
        weatherData
    }
}
