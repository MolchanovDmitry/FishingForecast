package dmitry.molchanov.domain.repository

import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.SimpleProfile
import kotlinx.coroutines.flow.Flow

interface ForecastSettingsRepository {

    fun fetchForecastSettingsFlow(profile: SimpleProfile?): Flow<List<ForecastSetting>>

    suspend fun fetchForecastSettings(profile: SimpleProfile?): List<ForecastSetting>

    suspend fun deleteForecastSetting(profile: SimpleProfile?, forecastSetting: ForecastSetting)

    suspend fun saveForecastSettings(profile: SimpleProfile?, forecastSetting: ForecastSetting)
}
