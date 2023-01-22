package dmitry.molchanov.fishingforecast.android.notifier

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dmitry.molchanov.fishingforecast.android.R.string
import dmitry.molchanov.weather_data_update.DataUpdateActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.showNotification(
            title = context.getString(string.notification_update_weather_data),
            message = context.getString(string.notification_update_weather_data_description)
        )
    }

    private fun Context.showNotification(title: String, message: String) {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val contentIntent = Intent(this, DataUpdateActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
        mNotificationManager.createNotificationChannel(channel)
        val mBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
    }

    private companion object {
        const val CHANNEL_ID = "dmitry.molchanov.fishingforecast.channel"
        const val CHANNEL_NAME = "channel name"
        const val NOTIFICATION_ID = 0
    }
}
