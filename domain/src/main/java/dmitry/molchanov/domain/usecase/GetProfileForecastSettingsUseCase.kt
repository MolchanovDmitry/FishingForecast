package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ForecastSettingsRepository
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

    suspend fun execute(profile: Profile): List<ForecastSetting> = withContext(ioDispatcher) {
        forecastSettingsRepository.fetchForecastSettings(profile as? SimpleProfile)
    }
}
