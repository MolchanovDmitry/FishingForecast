package dmitry.molchanov.data

import com.russhwolf.settings.ObservableSettings
import platform.Foundation.NSUserDefaults
import com.russhwolf.settings.AppleSettings as AppleSettings1

actual class AppSettings {

    actual val settings: ObservableSettings = AppleSettings1(NSUserDefaults.standardUserDefaults)
}