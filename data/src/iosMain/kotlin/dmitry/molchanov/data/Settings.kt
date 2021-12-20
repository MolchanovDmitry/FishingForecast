package dmitry.molchanov.data

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.ObservableSettings
import platform.Foundation.NSUserDefaults

actual class AppSettings {

    actual val settings: ObservableSettings = AppleSettings(NSUserDefaults.standardUserDefaults)
}