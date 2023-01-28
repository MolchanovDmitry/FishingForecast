package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ForecastSettingsRepository
import kotlinx.coroutines.withContext

class DeleteForecastSettingUseCase(private val repository: ForecastSettingsRepository) {

    suspend fun execute(profile: Profile, forecastSetting: ForecastSetting) =
        withContext(ioDispatcher) {
            repository.deleteForecastSetting(profile as? SimpleProfile, forecastSetting)
        }
}
