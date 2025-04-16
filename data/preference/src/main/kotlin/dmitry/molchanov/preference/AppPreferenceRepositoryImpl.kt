package dmitry.molchanov.preference

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.withContext
import dmitry.molchanov.core.DispatcherIo
import dmitry.molchanov.domain.repository.AppPreferenceRepository

class AppPreferenceRepositoryImpl(
    private val appSettings: ObservableSettings,
    private val dispatcherIo: DispatcherIo,
) : AppPreferenceRepository {

    override suspend fun getLastRequestTime(): Long? =
        withContext(dispatcherIo) {
            appSettings.getLongOrNull(LAST_REQUEST_TIME)
        }

    override suspend fun setLastRequestTime(lastRequestTime: Long): Unit =
        withContext(dispatcherIo) {
            appSettings[LAST_REQUEST_TIME] = lastRequestTime
        }

    private companion object {
        const val LAST_REQUEST_TIME = "LAST_REQUEST_TIME"
    }
}
