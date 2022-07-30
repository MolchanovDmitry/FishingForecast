package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.ResultDataRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

class SaveResultUseCase(private val resultDataRepository: ResultDataRepository) {

    suspend fun execute(resultName: String, weatherData: List<WeatherData>, profile: Profile, mapPoint: MapPoint) =
        withContext(ioDispatcher) {
            resultDataRepository.saveResult(
                mapPoint = mapPoint,
                resultName = resultName,
                weatherData = weatherData,
                profile = profile as? SimpleProfile,
            )
        }
}