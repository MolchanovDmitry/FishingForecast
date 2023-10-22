package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ProfileRepository

/**
 * Сохранить профиль в базу данных.
 */
class SaveProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(profile: SimpleProfile) =
        profileRepository.createProfile(profile.name)
}
