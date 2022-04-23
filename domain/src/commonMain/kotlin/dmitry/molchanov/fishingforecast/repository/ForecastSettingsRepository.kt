package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.Profile
import kotlinx.coroutines.flow.Flow

interface ForecastSettingsRepository {

    fun fetchForecastSettingsFlow(profile: Profile): Flow<List<ForecastSetting>>

    suspend fun fetchForecastSettings(profile: Profile): List<ForecastSetting>

    suspend fun deleteForecastSetting(profile: Profile, forecastSetting: ForecastSetting)

    suspend fun saveForecastSettings(profile: Profile?, forecastSetting: ForecastSetting)
}