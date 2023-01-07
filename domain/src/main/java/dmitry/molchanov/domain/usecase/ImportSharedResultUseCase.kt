package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.SharedResult
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

class ImportSharedResultUseCase(
    private val saveResultUseCase: SaveResultUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val saveMapPointUseCase: SaveMapPointUseCase,
    private val weatherDataRepository: WeatherDataRepository, //TODO заменить на usecase
) {

    suspend fun execute(sharedResults: List<SharedResult>) = withContext(ioDispatcher) {
        sharedResults.forEach {
            val result = it.result
            val mapPoint = result.mapPoint
            val profile = result.mapPoint.profile
            val weatherData = it.weatherData

            saveProfile(profile)
            saveMapPoint(mapPoint)
            saveWeatherData(weatherData)
            saveResultUseCase(it)
        }
    }

    private suspend fun saveProfile(profile: Profile) {
        (profile as? SimpleProfile)?.let {
            saveProfileUseCase.execute(it)
        }
    }

    private suspend fun saveMapPoint(mapPoint: MapPoint) {
        saveMapPointUseCase.execute(
            pointName = mapPoint.name,
            profile = mapPoint.profile,
            latitude = mapPoint.latitude,
            longitude = mapPoint.longitude
        )
    }

    private suspend fun saveWeatherData(weatherData: List<WeatherData>) {
        weatherDataRepository.saveWeatherData(weatherData)
    }

    private suspend fun saveResultUseCase(result: SharedResult) {
        saveResultUseCase.execute(
            resultName = result.result.name,
            weatherData = result.weatherData,
            profile = result.result.mapPoint.profile,
            mapPoint = result.result.mapPoint
        )
    }
}