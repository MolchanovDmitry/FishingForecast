package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.repository.ProfileRepository
import kotlinx.coroutines.withContext

/**
 * Удалить профиль.
 */
class DeleteProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(profile: Profile) = withContext(ioDispatcher) {
        profileRepository.deleteProfile(profile.name)
    }
}
