package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.Result
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.ResultDataRepository
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

class GetWeatherDataByResultUseCase(
    private val resultDataRepository: ResultDataRepository,
    private val weatherDataRepository: WeatherDataRepository
) {

    suspend fun execute(result: Result): List<WeatherData>  = withContext(ioDispatcher){
        val weatherDataIds = resultDataRepository.getWeatherDataIdsByResult(result)
        val weatherData = weatherDataRepository.getWeatherDataByIds(weatherDataIds)
        weatherData
    }
}