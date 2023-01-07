package dmitry.molchanov.db

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.domain.model.DeltaForecastMark
import dmitry.molchanov.domain.model.ExactValueForecastMark
import dmitry.molchanov.domain.model.ForecastMark
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.ForecastSettingsItem
import dmitry.molchanov.domain.model.MaxValueForecastMark
import dmitry.molchanov.domain.model.MinValueForecastMark
import dmitry.molchanov.domain.repository.ForecastSettingsRepository
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dmitry.molchanov.db.ForecastSetting as DataForecastSetting

class ForecastSettingRepositoryImpl(
    private val forecastSettingQueries: ForecastSettingQueries

) : ForecastSettingsRepository {

    override fun fetchForecastSettingsFlow(profile: SimpleProfile?): Flow<List<ForecastSetting>> =
        forecastSettingQueries.selectAll().asFlow().mapToList()
            .map { it.toDomainForecastSetting(profile) }

    override suspend fun fetchForecastSettings(profile: SimpleProfile?): List<ForecastSetting> =
        forecastSettingQueries.selectAll().executeAsList().toDomainForecastSetting(profile)

    override suspend fun deleteForecastSetting(profile: SimpleProfile?, forecastSetting: ForecastSetting) {
        forecastSettingQueries.delete(
            profileName = profile?.name,
            forecastSettingItemLabel = forecastSetting.forecastSettingsItem.name,
        )
    }

    override suspend fun saveForecastSettings(profile: SimpleProfile?, forecastSetting: ForecastSetting) {
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
                profileName = profile?.name,
                minValue = minValue,
                maxValue = maxValue,
                delta = deltaValue,
                exactValue = exactValue
            )
        )
    }

    private fun List<DataForecastSetting>.toDomainForecastSetting(profile: SimpleProfile?) =
        mapNotNull {
            if (it.profileName != profile?.name) return@mapNotNull null
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