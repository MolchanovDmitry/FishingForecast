package dmitry.molchanov.repository.profile

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.set
import dmitry.molchanov.core.DispatcherDefault
import dmitry.molchanov.core.DispatcherIo
import dmitry.molchanov.db.ProfileQueries
import dmitry.molchanov.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import dmitry.molchanov.db.Profile as DataProfile

class ProfileRepositoryImpl(
    private val profileQueries: ProfileQueries,
    private val appSettings: ObservableSettings, // TODO вынести в репозиторий
    private val dispatcherIo: DispatcherIo,
    mapDispatcher: DispatcherDefault,
) : ProfileRepository {
    override val profilesNamesFlow: Flow<List<String>> =
        profileQueries.selectAll()
            .asFlow()
            .flowOn(dispatcherIo)
            .mapToList(mapDispatcher)

    override val currentProfileNameFlow: Flow<String?> =
        appSettings.toFlowSettings()
            .getStringOrNullFlow(PROFILE_FLOW)
            .flowOn(dispatcherIo)

    override suspend fun getProfilesNames(): List<String> = withContext(dispatcherIo) {
        profileQueries.selectAll().executeAsList()
    }

    override suspend fun createProfile(profileName: String): Unit = withContext(dispatcherIo) {
        runCatching {
            profileQueries.insert(DataProfile(profileName))
        }
    }

    override suspend fun setCurrentProfileName(profileName: String?) = withContext(dispatcherIo) {
        appSettings[PROFILE_FLOW] = profileName
    }

    override suspend fun deleteProfile(profileName: String) = withContext(dispatcherIo) {
        profileQueries.delete(profileName)
    }

    private companion object {
        const val PROFILE_FLOW = "PROFILE_FLOW"
    }
}
