package dmitry.molchanov.fishingforecast.android.notifier

import dmitry.molchanov.domain.usecase.FetchAndSaveWeatherDataUseCase
import dmitry.molchanov.domain.usecase.GetMapPointsUseCase

/**
 * TODO актуализировать
 */
class WeatherNotifierPresenter(
    private val getCurrentWeatherDataUseCase: FetchAndSaveWeatherDataUseCase,
    private val getMapPointsUseCase: GetMapPointsUseCase,
    private val fetchAndSaveWeatherDataUseCase: FetchAndSaveWeatherDataUseCase,
) {

    suspend fun getForecast() {
        getMapPointsUseCase.execute().forEach { mapPoint ->
            /*val profile = getProfilesUseCase.execute()
                .firstOrNull { it.name == mapPoint.name || (mapPoint.profileName == null && it.isCommon) }
                ?: return@forEach*/
            getCurrentWeatherDataUseCase.execute(mapPoint)?.let { currentWeather ->
                // saveWeatherDataUseCase.execute(currentWeather)
            }
        }
    }
}
