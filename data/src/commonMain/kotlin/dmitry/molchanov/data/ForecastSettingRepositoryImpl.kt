package dmitry.molchanov.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.ForecastSettingQueries
import dmitry.molchanov.fishingforecast.model.*
import dmitry.molchanov.fishingforecast.repository.ForecastSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dmitry.molchanov.db.ForecastSetting as DataForecastSetting

class ForecastSettingRepositoryImpl(
    private val forecastSettingQueries: ForecastSettingQueries

) : ForecastSettingsRepository {

    override fun fetchForecastSettings(profile: Profile): Flow<List<ForecastSetting>> =
        forecastSettingQueries.selectAll().asFlow().mapToList()
            .map { it.toDomainForecastSetting(profile) }

    override suspend fun deleteForecastSetting(profile: Profile, forecastSetting: ForecastSetting) {
        forecastSettingQueries.delete(
            profileName = profile.name,
            forecastSettingItemLabel = forecastSetting.forecastSettingsItem.name,
        )
    }

    override suspend fun saveForecastSettings(profile: Profile?, forecastSetting: ForecastSetting) {
        var minValue: Double? = null
        var maxValue: Double? = null
        var deltaValue: Double? = null
        var exactValue: Double? = null
        forecastSetting.forecastMarks.forEach {
            when (it) {
                is DeltaForecastMark -> deltaValue = it.value.toDouble()
                is MinValueForecastMark -> minValue = it.value.toDouble()
                is MaxValueForecastMark -> maxValue = it.value.toDouble()
                is ExactValueForecastMark -> exactValue = it.value.toDouble()
            }
        }

        forecastSettingQueries.insert(
            DataForecastSetting(
                forecastSettingItemLabel = forecastSetting.forecastSettingsItem.name,
                profileName = profile?.name ?: "",
                minValue = minValue,
                maxValue = maxValue,
                delta = deltaValue,
                exactValue = exactValue
            )
        )
    }

    private fun List<DataForecastSetting>.toDomainForecastSetting(profile: Profile) =
        mapNotNull {
            if (it.profileName != profile.name) return@mapNotNull null
            val forecastMarks = mutableListOf<ForecastMark>()
            it.delta?.toFloat()?.let(::DeltaForecastMark)?.let(forecastMarks::add)
            it.minValue?.toFloat()?.let(::MinValueForecastMark)?.let(forecastMarks::add)
            it.maxValue?.toFloat()?.let(::MaxValueForecastMark)?.let(forecastMarks::add)
            it.exactValue?.toFloat()?.let(::ExactValueForecastMark)?.let(forecastMarks::add)
            ForecastSetting(
                forecastSettingsItem = ForecastSettingsItem.values()
                    .associateBy(ForecastSettingsItem::name)
                        [it.forecastSettingItemLabel]
                    ?: error("Невозможно соотнести forecastSettingItemLabel к ForecastSettingsItem"),
                forecastMarks = forecastMarks
            )
        }
}