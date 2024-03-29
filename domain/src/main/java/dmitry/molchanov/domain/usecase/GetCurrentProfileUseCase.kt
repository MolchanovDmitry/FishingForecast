package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.mapper.ProfileMapper
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetCurrentProfileUseCase(
    private val profileMapper: ProfileMapper,
    private val profileRepository: ProfileRepository
) {

    fun execute(): Flow<Profile> = profileRepository.currentProfileNameFlow
        .map(profileMapper::mapProfile)
        .flowOn(ioDispatcher)
}
