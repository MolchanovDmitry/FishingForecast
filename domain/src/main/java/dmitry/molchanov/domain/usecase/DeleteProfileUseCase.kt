package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.repository.ProfileRepository

/**
 * Удалить профиль.
 */
class DeleteProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(profile: Profile) {
        profileRepository.deleteProfile(profile.name)
    }
}
