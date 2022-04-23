package dmitry.molchanov.fishingforecast.android.notifier

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationCompat
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.utils.ONE_SEC
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

const val NOTIFICATION_ID = 1

class WeatherNotifierService : Service() {

    private val presenter by inject<WeatherNotifierPresenter>()
    private val handlerThread by lazy{ HandlerThread("Service").apply { start() }  }
    private val handler by lazy { Handler(handlerThread.looper) }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        setNotification(count, null, System.currentTimeMillis())
        startNotifierJob(null)
    }


    private fun startNotifierJob(time: Long?) {
        val firstTime = System.currentTimeMillis()
        handler.post{
            runBlocking {
                presenter.getForecast()
            }
        }
        count += 1
        val delay = ONE_SEC * 60 * 60
        setNotification(count, time, System.currentTimeMillis() + delay)
        handler.postDelayed(ONE_SEC * 60 * 60) {
            val last = System.currentTimeMillis()
            val totalTime = last - firstTime
            startNotifierJob(totalTime)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setNotification(count: Int = 0, time: Long?, nextTimeCheck: Long?) {
        val hours = time?.let {
            val seconds: Long = time / 1000
            val minutes = seconds / 60
            minutes / 60
        }
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val nextTimeCheckText = dateFormat.format(nextTimeCheck)
        val notification: Notification =
            NotificationCompat.Builder(this, getString(R.string.reminders_notification_channel_id))
                .setContentTitle(getText(R.string.notification_title))
                .setContentText("$count / $hours / $nextTimeCheckText")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    companion object {
        var count = 0
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