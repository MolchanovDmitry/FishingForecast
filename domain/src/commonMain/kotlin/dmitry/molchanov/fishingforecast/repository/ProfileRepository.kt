package dmitry.molchanov.fishingforecast.repository

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    val currentProfileNameFlow: Flow<String?>

    val profilesNamesFlow: Flow<List<String>>

    suspend fun getProfilesNames(): List<String>

    suspend fun createProfile(profileName: String)

    suspend fun setCurrentProfileName(profileName: String?)

    suspend fun deleteProfile(profileName: String)
}