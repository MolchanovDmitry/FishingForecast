package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ForecastSettingsRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Получить парочку элемент прогнозирования  - значение.
 * Например температура - минимальное значение(15.0)
 */
class GetForecastSettingMarksUseCase(private val repository: ForecastSettingsRepository) {

    suspend fun execute(profile: Profile): List<ForecastSetting> = withContext(ioDispatcher) {
        repository.fetchForecastSettings(profile)
    }
}