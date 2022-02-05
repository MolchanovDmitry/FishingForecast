package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.utils.TimeMs
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Получить список погодных показаний за период.
 */
class GetWeathersByPeriodUseCase(
    private val getSavedWeatherDataUseCase: GetSavedWeatherDataUseCase
) {

    /**
     * @param mapPoint точка на карте.
     * @param from начиная с какой даты смотреть.
     * @param to до какой даты смотреть.
     */
    suspend fun execute(mapPoint: MapPoint, from: TimeMs, to: TimeMs): Flow<List<WeatherData>> =
        withContext(ioDispatcher) {
            getSavedWeatherDataUseCase.execute(mapPoint, from, to)
        }
}