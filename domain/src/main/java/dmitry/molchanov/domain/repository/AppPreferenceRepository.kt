package dmitry.molchanov.domain.repository

interface AppPreferenceRepository {

    suspend fun getLastRequestTime(): Long?

    suspend fun setLastRequestTime(lastRequestTime: Long)
}
