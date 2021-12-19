package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Получить доступные профили из базы данных.
 */
class GetProfilesUseCase(private val profileRepository: ProfileRepository) {

    fun execute(): Flow<List<String>> = profileRepository.getProfilesFlow().flowOn(ioDispatcher)
}