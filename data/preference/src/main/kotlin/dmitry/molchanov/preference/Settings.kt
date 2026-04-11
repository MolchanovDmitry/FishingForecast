@file:Suppress("MatchingDeclarationName")

package dmitry.molchanov.preference

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.ObservableSettings

class AppSettings(context: Context) {
    val settings: ObservableSettings = SharedPreferencesSettings(
        context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    )
}
