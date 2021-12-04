package dmitry.molchanov.fishingforecast.usecase

import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Получить все точки на карте в профиле.
 */
class GetMapPointsTest {

    private val repository = mock(MapPointRepository::class.java)
    private val getMapPointUseCase = GetMapPointsUseCase(repository)

    @Test
    fun execute(): Unit = runBlocking {
        getMapPointUseCase.execute()
        verify(repository).fetchMapPoints()
    }
}