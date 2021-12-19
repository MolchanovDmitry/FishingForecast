package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfilesFlow(): Flow<List<String>>

    suspend fun createProfile(profile: Profile)

    suspend fun fetchCurrentProfile(): Profile

    suspend fun saveCurrentProfile(profile: Profile)

    suspend fun deleteProfile(profile: Profile)
}