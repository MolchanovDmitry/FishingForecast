package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.domain.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Удалить профиль.
 */
class DeleteProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(profile: Profile) = withContext(ioDispatcher) {
        profileRepository.deleteProfile(profile.name)
    }

}