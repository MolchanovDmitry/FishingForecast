package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.mapper.CommonProfileFetcher
import dmitry.molchanov.domain.mapper.ProfileMapper
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Получить доступные профили из базы данных.
 */
class GetProfilesUseCase(
    private val profileMapper: ProfileMapper,
    private val profileRepository: ProfileRepository,
    private val commonProfileFetcher: CommonProfileFetcher
) {

    fun executeFlow(): Flow<List<Profile>> = profileRepository.profilesNamesFlow
        .map { profileNames ->
            profileNames.map(::SimpleProfile) + commonProfileFetcher.instance
        }
        .flowOn(ioDispatcher)

    suspend fun execute(): List<Profile> = withContext(ioDispatcher) {
        profileMapper.mapToProfiles(profileRepository.getProfilesNames())
    }
}
