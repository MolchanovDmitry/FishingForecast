package dmitry.molchanov.domain.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Тесты модели Result с рейтингом.
 */
class ResultTest {

    @Test
    fun `test result with rating`() {
        val mapPoint = dmitry.molchanov.domain.model.MapPoint(
            id = 1,
            name = "Test Point",
            profile = dmitry.molchanov.domain.model.CommonProfile(name = "Test"),
            latitude = 55.0,
            longitude = 37.0
        )
        val result = Result(id = 1, name = "Test Result", mapPoint = mapPoint, rating = 4)
        assertEquals(4, result.rating)
    }

    @Test
    fun `test result without rating`() {
        val mapPoint = dmitry.molchanov.domain.model.MapPoint(
            id = 1,
            name = "Test Point",
            profile = dmitry.molchanov.domain.model.CommonProfile(name = "Test"),
            latitude = 55.0,
            longitude = 37.0
        )
        val result = Result(id = 1, name = "Test Result", mapPoint = mapPoint)
        assertNull(result.rating)
    }

    @Test
    fun `test result serialization with rating`() {
        val mapPoint = dmitry.molchanov.domain.model.MapPoint(
            id = 1,
            name = "Test Point",
            profile = dmitry.molchanov.domain.model.CommonProfile(name = "Test"),
            latitude = 55.0,
            longitude = 37.0
        )
        val result = Result(id = 1, name = "Test Result", mapPoint = mapPoint, rating = 3)
        val json = Json.encodeToString(result)
        assert(json.contains("\"rating\":3"))
    }

    @Test
    fun `test result serialization without rating`() {
        val mapPoint = dmitry.molchanov.domain.model.MapPoint(
            id = 1,
            name = "Test Point",
            profile = dmitry.molchanov.domain.model.CommonProfile(name = "Test"),
            latitude = 55.0,
            longitude = 37.0
        )
        val result = Result(id = 1, name = "Test Result", mapPoint = mapPoint)
        val json = Json.encodeToString(result)
        assert(!json.contains("\"rating\""))
    }
}
