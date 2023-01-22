package dmitry.molchanov.weather_data_update

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*


fun Context.schedule(hour: Int, minute: Int, intent: Intent) {
    if (hour !in 0..24) {
        error("Часы должны быть в предалах от 0 до 24.")
    }
    if (minute !in 0..60) {
        error("Минуты должны быть в пределах от 0 до 60.")
    }
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar[Calendar.HOUR_OF_DAY] = hour
    calendar[Calendar.MINUTE] = minute
    calendar[Calendar.SECOND] = 0

    val notifyPendingIntent = PendingIntent.getBroadcast(
        this,
        1,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    ////If the Toggle is turned on, set the repeating alarm with a 15 minute interval
    val repeatInterval = 1 * 60 * 1000L
    alarmManager.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        repeatInterval,
        notifyPendingIntent
    )
}