package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.ResultDataRepository
import dmitry.molchanov.domain.repository.WeatherDataRepository

class GetWeatherDataByResultUseCase(
    private val resultDataRepository: ResultDataRepository,
    private val weatherDataRepository: WeatherDataRepository,
) {

    suspend fun execute(result: Result): List<WeatherData> {
        val weatherDataIds = resultDataRepository.getWeatherDataIdsByResult(result)
        val weatherData = weatherDataRepository.getWeatherDataByIds(weatherDataIds)
        return weatherData
    }
}
