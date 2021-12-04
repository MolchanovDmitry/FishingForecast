package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.Profile

interface ForecastSettingsRepository {

    suspend fun fetchForecastSettings(profile: Profile): List<ForecastSetting>

    suspend fun saveForecastSettings(profile: Profile, forecastSettings: List<ForecastSetting>)
}