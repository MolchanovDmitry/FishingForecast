package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.domain.repository.ProfileRepository

class SelectProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun execute(profile: Profile) {
        (profile as? SimpleProfile)?.name
            .let { nullableName -> profileRepository.setCurrentProfileName(nullableName) }
    }
}
