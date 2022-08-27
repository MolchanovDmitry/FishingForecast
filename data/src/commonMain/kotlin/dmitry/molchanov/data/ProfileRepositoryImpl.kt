package dmitry.molchanov.data

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.set
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.ProfileQueries
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import dmitry.molchanov.db.Profile as DataProfile


class ProfileRepositoryImpl(
    private val profileQueries: ProfileQueries,
    private val appSettings: ObservableSettings // TODO вынести в репозиторий
) : ProfileRepository {

    override val profilesNamesFlow: Flow<List<String>> =
        profileQueries.selectAll().asFlow().mapToList()

    override val currentProfileNameFlow: Flow<String?> =
        appSettings.toFlowSettings().getStringOrNullFlow(PROFILE_FLOW)

    override suspend fun getProfilesNames(): List<String> {
        return profileQueries.selectAll().executeAsList()
    }

    override suspend fun createProfile(profileName: String) {
        runCatching {
            profileQueries.insert(DataProfile(profileName))
        }
    }

    override suspend fun setCurrentProfileName(profileName: String?) {
        appSettings[PROFILE_FLOW] = profileName
    }

    override suspend fun deleteProfile(profileName: String) {
        profileQueries.delete(profileName)
    }

    private companion object {
        const val PROFILE_FLOW = "PROFILE_FLOW"
    }
}