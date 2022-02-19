package dmitry.molchanov.fishingforecast.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dmitry.molchanov.fishingforecast.android.notifier.WeatherNotifierService
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(vm)
        }

        createNotificationsChannels()
        runNotifierService()
    }

    private fun runNotifierService() {
        WeatherNotifierService.start(context = this)
    }

    private fun createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.reminders_notification_channel_id)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            if (notificationManager?.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId,
                    getString(R.string.reminders_notification_channel_name),
                    NotificationManager.IMPORTANCE_LOW
                )
                ContextCompat.getSystemService(this, NotificationManager::class.java)
                    ?.createNotificationChannel(channel)
            }
        }
    }
}
