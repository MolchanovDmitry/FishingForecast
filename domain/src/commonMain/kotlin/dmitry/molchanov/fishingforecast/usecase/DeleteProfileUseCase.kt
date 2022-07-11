package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * Удалить профиль.
 */
class DeleteProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(profile: Profile): Result<Unit> = withContext(ioDispatcher) {
        if (!profile.isCommon) {
            profileRepository.deleteProfile(profile)
            Result.success(Unit)
        } else {
            Result.failure(MainProfileDeleteException)
        }
    }

    object MainProfileDeleteException : Exception("Can't delete a common profile")
}