package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.domain.repository.ForecastSettingsRepository
import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Сохранить показатель прогнозирования, например:
 * температура, минимальное значение - 15 градусов.
 */
class SaveForecastSettingMarkUseCase(private val repository: ForecastSettingsRepository) {

    /**
     * @param profile профиль, для которого сохранить настройку.
     * @param forecastSetting настройка прогнозирования.
     */
    suspend fun execute(
        profile: SimpleProfile?,
        forecastSetting: ForecastSetting,
    ) = withContext(ioDispatcher) {
        repository.saveForecastSettings(profile, forecastSetting)
    }
}