package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.flowOn

class GetCurrentProfileUseCase(private val profileRepository: ProfileRepository) {

    fun execute() = profileRepository.currentProfileFlow.flowOn(ioDispatcher)
}