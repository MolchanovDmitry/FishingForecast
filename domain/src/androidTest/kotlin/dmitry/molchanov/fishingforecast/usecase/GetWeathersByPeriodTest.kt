package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.utils.nowUnixTime
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Получить список погодных показаний за период.
 */
class GetWeathersByPeriodTest {

    private val weatherDataRepository = mock(WeatherDataRepository::class.java)
    private val getSavedWeatherDataUseCase = GetSavedWeatherDataUseCase(weatherDataRepository)

    @Test
    fun execute(): Unit = runBlocking {
        val mapPoint = MapPoint("", 1, 4)
        val from = nowUnixTime
        val to = nowUnixTime + 777
        getSavedWeatherDataUseCase.execute(mapPoint, from, to)
        verify(weatherDataRepository).fetchWeatherData(mapPoint, from, to)
    }

}