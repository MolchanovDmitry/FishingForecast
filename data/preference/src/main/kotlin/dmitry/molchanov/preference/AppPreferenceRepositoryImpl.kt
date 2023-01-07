package dmitry.molchanov.preference

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.set
import dmitry.molchanov.domain.repository.AppPreferenceRepository

class AppPreferenceRepositoryImpl(
    private val appSettings: ObservableSettings
) : AppPreferenceRepository {

    override var lastRequestTime: Long?
        get() = appSettings.getLongOrNull(LAST_REQUEST_TIME)
        set(value) = appSettings.set(LAST_REQUEST_TIME, value)

    private companion object {
        const val LAST_REQUEST_TIME = "LAST_REQUEST_TIME"
    }
}