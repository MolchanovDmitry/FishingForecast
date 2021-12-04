package dmitry.molchanov.fishingforecast.repository

import dmitry.molchanov.fishingforecast.model.Profile

interface ProfileRepository {
    
    suspend fun fetchProfile(): List<Profile>

    suspend fun createProfile(profile: Profile)

    suspend fun fetchCurrentProfile(): Profile

    suspend fun saveCurrentProfile(profile: Profile)
}