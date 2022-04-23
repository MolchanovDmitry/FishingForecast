package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ForecastSettingsRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Получить парочку элемент прогнозирования  - значение.
 * Например температура - минимальное значение(15.0)
 */
class GetForecastSettingMarksUseCase(private val repository: ForecastSettingsRepository) {

    fun execute(profile: Profile): Flow<List<ForecastSetting>> =
        repository.fetchForecastSettingsFlow(profile).flowOn(ioDispatcher)
}