package dmitry.molchanov.fishingforecast.android.notifier

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dmitry.molchanov.fishingforecast.android.R

const val NOTIFICATION_ID = 1

class WeatherNotifierService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        val notification: Notification =
            NotificationCompat.Builder(this, getString(R.string.reminders_notification_channel_id))
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, WeatherNotifierService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.applicationContext.startForegroundService(intent)
            } else {
                context.applicationContext.startService(intent)
            }
        }
    }
}