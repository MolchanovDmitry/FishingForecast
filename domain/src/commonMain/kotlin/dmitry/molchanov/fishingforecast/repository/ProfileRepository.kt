package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfilesFlow(): Flow<List<Profile>>

    val currentProfileFlow: Flow<Profile>

    suspend fun createProfile(profile: Profile)

    suspend fun setCurrentProfile(profile: Profile)

    suspend fun deleteProfile(profile: Profile)
}