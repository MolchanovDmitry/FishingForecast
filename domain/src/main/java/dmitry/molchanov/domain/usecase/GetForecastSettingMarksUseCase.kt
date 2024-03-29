package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ForecastSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Получить парочку элемент прогнозирования  - значение.
 * Например температура - минимальное значение(15.0)
 */
class GetForecastSettingMarksUseCase(private val repository: ForecastSettingsRepository) {

    suspend fun execute(profile: Profile): List<ForecastSetting> = withContext(ioDispatcher) {
        repository.fetchForecastSettings(profile as? SimpleProfile)
    }

    fun executeFlow(profile: Profile): Flow<List<ForecastSetting>> =
        repository.fetchForecastSettingsFlow(profile as? SimpleProfile)
            .flowOn(ioDispatcher)
}
