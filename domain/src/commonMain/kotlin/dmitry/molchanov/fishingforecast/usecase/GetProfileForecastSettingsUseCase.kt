package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.repository.ForecastSettingsRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Получить список элементов прогнозирования со значениями для профиля.
 */
class GetProfileForecastSettingsUseCase(
    private val forecastSettingsRepository: ForecastSettingsRepository
) {
     fun executeFlow(profile: Profile): Flow<List<ForecastSetting>> =
            forecastSettingsRepository.fetchForecastSettingsFlow(profile as? SimpleProfile)

    suspend fun execute(profile: Profile): List<ForecastSetting> =
        withContext(ioDispatcher) {
            forecastSettingsRepository.fetchForecastSettings(profile as? SimpleProfile)
        }
}