package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.domain.repository.ForecastSettingsRepository
import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

class DeleteForecastSettingUseCase(private val repository: ForecastSettingsRepository) {

    suspend fun execute(profile: Profile, forecastSetting: ForecastSetting) =
        withContext(ioDispatcher) {
            repository.deleteForecastSetting(profile as? SimpleProfile, forecastSetting)
        }
}