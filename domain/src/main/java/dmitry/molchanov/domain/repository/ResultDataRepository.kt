package dmitry.molchanov.domain.repository

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.SimpleProfile
import kotlinx.coroutines.flow.Flow

interface ResultDataRepository {

    suspend fun saveResult(
        resultName: String,
        weatherDataIds: List<Long>,
        profile: SimpleProfile?,
        mapPoint: MapPoint,
        rating: Int? = null,
        description: String? = null
    )

    fun getResultsFlow(): Flow<List<Result>>
    suspend fun getWeatherDataIdsByResult(result: Result): List<Long>

    /** Получить минимальную дату погоды, привязанную к результату */
    suspend fun getMinWeatherDateByResult(resultId: Long): Long?

    /** Получить максимальную дату погоды, привязанную к результату */
    suspend fun getMaxWeatherDateByResult(resultId: Long): Long?

    /** Обновить имя результата */
    suspend fun updateResultName(resultId: Long, newName: String)

    /** Обновить рейтинг результата */
    suspend fun updateResultRating(resultId: Long, rating: Int)

    /** Обновить описание результата */
    suspend fun updateResultDescription(resultId: Long, description: String)

    /** Получить результаты отсортированные по дате */
    fun getResultsFlowOrderByDate(): Flow<List<Result>>

    /** Получить результаты отсортированные по рейтингу */
    fun getResultsFlowOrderByRating(): Flow<List<Result>>
}

class NullWeatherData : IllegalStateException("Weather date are null")
class NullResultId : IllegalStateException("Last inserted result row not founded")
