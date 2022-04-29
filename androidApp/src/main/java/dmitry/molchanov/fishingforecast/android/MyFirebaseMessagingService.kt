package dmitry.molchanov.fishingforecast.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dmitry.molchanov.fishingforecast.android.notifier.WeatherNotifierPresenter
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

const val NOTIFICATION_ID = 1

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val presenter by inject<WeatherNotifierPresenter>()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        createNotificationsChannels()
        setNotification()
        checkWeather()
    }

    private fun checkWeather() = scope.launch {
        presenter.getForecast()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun setNotification() {
        val notification: Notification =
            NotificationCompat.Builder(this, getString(R.string.reminders_notification_channel_id))
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_title))
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .build()
        startForeground(NOTIFICATION_ID, notification)
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
                channel.setSound(null, null)
                ContextCompat.getSystemService(this, NotificationManager::class.java)
                    ?.createNotificationChannel(channel)
            }
        }
    }
}