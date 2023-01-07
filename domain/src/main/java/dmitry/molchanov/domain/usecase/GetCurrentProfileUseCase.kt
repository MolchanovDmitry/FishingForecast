package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.mapper.ProfileMapper
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetCurrentProfileUseCase(
    private val profileMapper: ProfileMapper,
    private val profileRepository: ProfileRepository
) {

    fun execute(): Flow<Profile> = profileRepository.currentProfileNameFlow
        .map (profileMapper::mapProfile)
        .flowOn(ioDispatcher)
}
