package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Сохранить профиль в базу данных.
 */
class SaveProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(profile: SimpleProfile) = withContext(ioDispatcher) {
        profileRepository.createProfile(profile.name)
    }
}
