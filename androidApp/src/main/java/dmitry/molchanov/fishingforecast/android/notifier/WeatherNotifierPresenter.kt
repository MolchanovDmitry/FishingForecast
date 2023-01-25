package dmitry.molchanov.fishingforecast.android.notifier

import dmitry.molchanov.domain.usecase.GetMapPointsUseCase
import dmitry.molchanov.domain.usecase.SaveWeatherDataUseCase

/**
 * TODO актуализировать
 */
class WeatherNotifierPresenter(
    private val getCurrentWeatherDataUseCase: SaveWeatherDataUseCase,
    private val getMapPointsUseCase: GetMapPointsUseCase,
    private val saveWeatherDataUseCase: SaveWeatherDataUseCase,
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
