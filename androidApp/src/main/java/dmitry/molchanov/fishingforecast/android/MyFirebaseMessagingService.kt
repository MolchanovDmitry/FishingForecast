package dmitry.molchanov.fishingforecast.android

import android.app.Notification
import androidx.core.app.NotificationCompat
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
}