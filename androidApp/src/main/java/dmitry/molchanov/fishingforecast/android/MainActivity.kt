package dmitry.molchanov.fishingforecast.android

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dmitry.molchanov.fishingforecast.android.notifier.AlarmReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(vm)
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SCHEDULE_EXACT_ALARM
                ) != PackageManager.PERMISSION_GRANTED -> {
                    val requestPermissionLauncher =
                        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                            if (isGranted) initAlarm() else requestPermissions()
                        }
                    requestPermissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
                }
                else -> initAlarm()
            }
        } else {
            initAlarm()
        }
    }

    private fun initAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        // val intent = Intent(applicationContext, DataUpdateActivity::class.java)
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 3)
        }
        /*alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )*/
        // alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent)

        /*alarmManager.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000 * 3,
            pendingIntent
        )*/

        /*alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
            60 * 1000,
            pendingIntent
        )*/
        val triggerTime = SystemClock.elapsedRealtime() + 1000 * 5

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            1000 * 5,
            pendingIntent
        )
    }
}
