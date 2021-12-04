package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Получить доступные профили из базы данных.
 */
class GetProfilesUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(): List<Profile> = withContext(ioDispatcher) {
        profileRepository.fetchProfile()
    }
}