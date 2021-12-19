package dmitry.molchanov.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.AppDatabase
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(private val dataBase: AppDatabase) : ProfileRepository {

    override fun getProfilesFlow(): Flow<List<String>> =
        dataBase.dbSampleQueries.selectAll().asFlow().mapToList()


    override suspend fun createProfile(profile: Profile) {
        runCatching {
            dataBase.dbSampleQueries.insert(dmitry.molchanov.db.Profile(profile.name))
        }

    }

    override suspend fun fetchCurrentProfile(): Profile {
        TODO("Not yet implemented")
    }

    override suspend fun saveCurrentProfile(profile: Profile) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProfile(profile: Profile) {
        dataBase.dbSampleQueries.delete(profile.name)
    }
}