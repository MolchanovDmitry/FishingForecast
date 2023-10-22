package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ForecastSettingsRepository

class DeleteForecastSettingUseCase(private val repository: ForecastSettingsRepository) {

    suspend fun execute(profile: Profile, forecastSetting: ForecastSetting) =
        repository.deleteForecastSetting(profile as? SimpleProfile, forecastSetting)
}
