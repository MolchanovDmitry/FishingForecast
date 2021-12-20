package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.commonProfile
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.utils.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * Получить доступные профили из базы данных.
 */
class GetProfilesUseCase(private val profileRepository: ProfileRepository) {

    fun execute(): Flow<List<Profile>> = profileRepository.getProfilesFlow()
        .map {
            mutableListOf<Profile>().apply {
                add(commonProfile)
                addAll(it)
            }
        }
        .flowOn(ioDispatcher)
}