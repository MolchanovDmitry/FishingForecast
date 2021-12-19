package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Получить доступные профили из базы данных.
 */
class GetProfilesTest {

    private val profileRepository = mock(ProfileRepository::class.java)
    private val getProfilesUseCase = GetProfilesUseCase(profileRepository)

    @Test
    fun execute(): Unit = runBlocking {
        getProfilesUseCase.execute()
        verify(profileRepository).fetchProfiles()
    }
}