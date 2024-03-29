package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.ResultDataRepository
import dmitry.molchanov.domain.repository.WeatherDataRepository
import kotlinx.coroutines.withContext

class SaveResultUseCase(
    private val resultDataRepository: ResultDataRepository,
    private val weatherDataRepository: WeatherDataRepository,
) {

    suspend fun execute(resultName: String, weatherData: List<WeatherData>, profile: Profile, mapPoint: MapPoint) =
        withContext(ioDispatcher) {
            val weatherDataIds = weatherDataRepository.getWeatherDataIds(weatherData)
            resultDataRepository.saveResult(
                mapPoint = mapPoint,
                resultName = resultName,
                weatherDataIds = weatherDataIds,
                profile = profile as? SimpleProfile,
            )
        }
}