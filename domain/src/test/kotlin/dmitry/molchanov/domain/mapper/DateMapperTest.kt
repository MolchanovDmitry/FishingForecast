package dmitry.molchanov.domain.mapper

import java.text.SimpleDateFormat
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Проверяем парсинг даты
 */
@Suppress("PrivatePropertyName")
class DateMapperTest {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")

    private val date_2023_12_25 = dateFormat.parse("25-12-2023").time
    private val weatherDate_2023_12_25 = date_2023_12_25.toWeatherDate()

    private val date_2024_02_29 = dateFormat.parse("29-02-2024").time
    private val weatherDate_2024_02_29 = date_2024_02_29.toWeatherDate()

    @Test
    fun testRaw(){
        assertEquals(date_2023_12_25, weatherDate_2023_12_25.raw)
        assertEquals(date_2024_02_29, weatherDate_2024_02_29.raw)
    }

    @Test
    fun testYear(){
        assertEquals(2023, weatherDate_2023_12_25.year)
        assertEquals(2024, weatherDate_2024_02_29.year)
    }

    @Test
    fun testMonth(){
        assertEquals(12, weatherDate_2023_12_25.month)
        assertEquals(2, weatherDate_2024_02_29.month)
    }

    @Test
    fun testDay(){
        assertEquals(25, weatherDate_2023_12_25.day)
        assertEquals(29, weatherDate_2024_02_29.day)
    }
}