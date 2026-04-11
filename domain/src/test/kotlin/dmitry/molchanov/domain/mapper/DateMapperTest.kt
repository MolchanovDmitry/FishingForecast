package dmitry.molchanov.domain.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.TimeZone

/**
 * Проверяем парсинг даты
 */
@Suppress("PrivatePropertyName")
class DateMapperTest {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy").apply {
        timeZone = TimeZone.getDefault()
    }

    private val _date_2023_12_25 = dateFormat.parse("25-12-2023").time
    private val _weatherDate_2023_12_25 = _date_2023_12_25.toWeatherDate()

    private val _date_2024_02_29 = dateFormat.parse("29-02-2024").time
    private val _weatherDate_2024_02_29 = _date_2024_02_29.toWeatherDate()

    @Test
    fun testRaw() {
        // roundedValue — корректное миллисекундное значение
        assertNotNull(_weatherDate_2023_12_25.roundedValue)
        assertNotNull(_weatherDate_2024_02_29.roundedValue)
    }

    @Test
    fun testYear() {
        assertEquals(2023, _weatherDate_2023_12_25.year)
        assertEquals(2024, _weatherDate_2024_02_29.year)
    }

    @Test
    fun testMonth() {
        assertEquals(12, _weatherDate_2023_12_25.month)
        assertEquals(2, _weatherDate_2024_02_29.month)
    }

    @Test
    fun testDay() {
        assertEquals(25, _weatherDate_2023_12_25.day)
        assertEquals(29, _weatherDate_2024_02_29.day)
    }
}
