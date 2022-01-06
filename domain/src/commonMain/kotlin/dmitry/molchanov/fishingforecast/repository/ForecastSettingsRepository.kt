package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.Profile
import kotlinx.coroutines.flow.Flow

interface ForecastSettingsRepository {

    fun fetchForecastSettings(profile: Profile): Flow<List<ForecastSetting>>

    suspend fun saveForecastSettings(profile: Profile?, forecastSetting: ForecastSetting)
}