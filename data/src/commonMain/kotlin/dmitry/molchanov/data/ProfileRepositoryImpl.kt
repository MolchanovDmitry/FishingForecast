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


class ProfileRepositoryImpl(
    private val profileQueries: ProfileQueries,
    private val appSettings: ObservableSettings
) : ProfileRepository {

    override fun getProfilesFlow(): Flow<List<Profile>> =
        profileQueries.selectAll().asFlow().mapToList().map { it.map(::Profile) }

    override val currentProfileFlow: Flow<Profile> =
        appSettings.toFlowSettings().getStringFlow(PROFILE_FLOW).map(::Profile)

    override suspend fun createProfile(profile: Profile) {
        runCatching {
            profileQueries.insert(dmitry.molchanov.db.Profile(profile.name))
        }
    }

    override suspend fun setCurrentProfile(profile: Profile) {
        println("1488 setCurrentProfile = ${profile}")
        appSettings[PROFILE_FLOW] = profile.name
        println("1488 setCurrentProfile after = ${appSettings.getString(PROFILE_FLOW)}")
    }

    override suspend fun deleteProfile(profile: Profile) {
        profileQueries.delete(profile.name)
    }

    private companion object {
        const val PROFILE_FLOW = "PROFILE_FLOW"
    }
}