package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import dmitry.molchanov.fishingforecast.utils.nowUnixTime
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Получить сохраненное значение погоды по точке на карте.
 */
class GetSavedWeatherDataTest {

    private val repository = mock(WeatherDataRepository::class.java)
    private val getSavedWeatherDataUseCase = GetSavedWeatherDataUseCase(repository)

    @Test
    fun execute(): Unit = runBlocking {
        val mapPoint = MapPoint("", 3, 4)
        val from = nowUnixTime
        val to = nowUnixTime + 777
        getSavedWeatherDataUseCase.execute(mapPoint, from, to)
        verify(repository).fetchWeatherData(mapPoint, from, to)
    }
}