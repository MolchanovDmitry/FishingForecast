package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

class SelectProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(profile: Profile) = withContext(ioDispatcher) {
        (profile as? SimpleProfile)?.name
            .let { nullableName -> profileRepository.setCurrentProfileName(nullableName) }
    }
}
