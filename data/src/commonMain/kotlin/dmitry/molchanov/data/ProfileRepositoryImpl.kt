package dmitry.molchanov.data

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.set
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.ProfileQueries
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dmitry.molchanov.db.Profile as DataProfile


class ProfileRepositoryImpl(
    private val profileQueries: ProfileQueries,
    private val appSettings: ObservableSettings // TODO вынести в репозиторий
) : ProfileRepository {

    override fun getProfilesFlow(): Flow<List<Profile>> =
        profileQueries.selectAll().asFlow().mapToList().map {
            it.map { dataProfile -> dataProfile.toDomainProfile() }
        }

    override suspend fun getProfiles(): List<Profile> {
        return profileQueries.selectAll().executeAsList().map { it.toDomainProfile() }
    }

    override val currentProfileFlow: Flow<Profile> =
        appSettings.toFlowSettings().getStringFlow(PROFILE_FLOW).map {
            Profile(name = it, isCommon = it.isEmpty())
        }

    override suspend fun createProfile(profile: Profile) {
        runCatching {
            profileQueries.insert(DataProfile(profile.name, isCommon = false))
        }
    }

    override suspend fun setCurrentProfile(profile: Profile) {
        appSettings[PROFILE_FLOW] = profile.name
    }

    override suspend fun deleteProfile(profile: Profile) {
        profileQueries.delete(profile.name)
    }

    private fun DataProfile.toDomainProfile() = Profile(name = this.name, isCommon = this.isCommon)

    private companion object {
        const val PROFILE_FLOW = "PROFILE_FLOW"
    }
}