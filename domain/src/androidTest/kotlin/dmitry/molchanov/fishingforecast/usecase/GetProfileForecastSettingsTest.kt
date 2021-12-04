package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ForecastSettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class GetProfileForecastSettingsTest {

    private val repository = mock(ForecastSettingsRepository::class.java)
    private val getProfileForecastUseCase = GetProfileForecastSettingsUseCase(repository)

    @Test
    fun execute(): Unit = runBlocking {
        val profile = Profile("some profile")
        getProfileForecastUseCase.execute(profile)
        verify(repository).fetchForecastSettings(profile)
    }
}