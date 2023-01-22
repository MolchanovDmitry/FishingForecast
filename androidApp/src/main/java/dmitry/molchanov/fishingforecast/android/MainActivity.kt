package dmitry.molchanov.fishingforecast.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dmitry.molchanov.fishingforecast.android.notifier.AlarmReceiver
import dmitry.molchanov.weather_data_update.schedule
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(vm)
        }
        schedule(
            hour = 13,
            minute = 0,
            intent = Intent(this, AlarmReceiver::class.java)
        )
    }
}
