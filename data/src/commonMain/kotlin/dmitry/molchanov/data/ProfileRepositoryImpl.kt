package dmitry.molchanov.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.ProfileQueries
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(private val profileQueries: ProfileQueries) : ProfileRepository {

    override fun getProfilesFlow(): Flow<List<String>> =
        profileQueries.selectAll().asFlow().mapToList()


    override suspend fun createProfile(profile: Profile) {
        runCatching {
            profileQueries.insert(dmitry.molchanov.db.Profile(profile.name))
        }
    }

    override suspend fun fetchCurrentProfile(): Profile {
        TODO("Not yet implemented")
    }

    override suspend fun saveCurrentProfile(profile: Profile) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProfile(profile: Profile) {
        profileQueries.delete(profile.name)
    }
}