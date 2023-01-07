package dmitry.molchanov.preference

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ObservableSettings

class AppSettings(context: Context) {
    val settings: ObservableSettings = AndroidSettings(
        context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    )
}