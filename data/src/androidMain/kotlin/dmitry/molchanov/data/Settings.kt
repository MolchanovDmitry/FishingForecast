package dmitry.molchanov.data

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ObservableSettings

actual class AppSettings(context: Context) {

    actual val settings: ObservableSettings = AndroidSettings(
        context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    )
}