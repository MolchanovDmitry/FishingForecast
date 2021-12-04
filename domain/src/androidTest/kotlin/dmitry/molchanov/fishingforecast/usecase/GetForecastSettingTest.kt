package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.repository.ForecastSettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class GetForecastSettingTest {

    private val repository = mock(ForecastSettingsRepository::class.java)
    private val getForecastSettingMarksUseCase = GetForecastSettingMarksUseCase(repository)

    @Test
    fun execute(): Unit = runBlocking {
        val profile = Profile("some profile")
        getForecastSettingMarksUseCase.execute(profile)
        verify(repository).fetchForecastSettings(profile)
    }
}