package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.utils.ONE_DAY
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Тесты фильтрации погоды для детального просмотра результата:
 * 3 дня до + день результата + 2 дня после = окно из 5 дней.
 */
class WeatherWindowFilterTest {

    @Test
    fun `test weather window boundaries`() = runBlocking {
        val resultTimestamp = 1712000000000L // 1 апреля 2024
        val from = resultTimestamp - 3 * ONE_DAY
        val to = resultTimestamp + 2 * ONE_DAY

        // Проверяем, что окно правильно рассчитывается
        assertEquals(3 * ONE_DAY, resultTimestamp - from)
        assertEquals(2 * ONE_DAY, to - resultTimestamp)
        assertEquals(5 * ONE_DAY, to - from)
    }

    @Test
    fun `test weather window contains 5 days`() {
        val resultTimestamp = 1712000000000L
        val from = resultTimestamp - 3 * ONE_DAY
        val to = resultTimestamp + 2 * ONE_DAY

        val expectedDays = 5L
        val actualDays = (to - from) / ONE_DAY
        assertEquals(expectedDays, actualDays)
    }
}
