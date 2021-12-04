package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.repository.YandexWeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class GetCurrentWeatherDataTest {

    private val yandexWeatherRepository = mock(YandexWeatherRepository::class.java)
    private val getCurrentWeatherDataUseCase = GetCurrentWeatherDataUseCase(yandexWeatherRepository)

    @Test
    fun execute(): Unit = runBlocking {
        val mapPoint = MapPoint("", 1, 1)
        getCurrentWeatherDataUseCase.execute(mapPoint)
        verify(yandexWeatherRepository).getCurrentWeatherWithForecast(mapPoint)
    }
}