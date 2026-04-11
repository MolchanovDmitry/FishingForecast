package dmitry.molchanov.fishingforecast.android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import dmitry.molchanov.fishingforecast.android.notifier.AlarmReceiver
import dmitry.molchanov.weather_data_update.schedule
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                vm.messageFlow.collect { message ->
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
            MainScreen(vm)
        }
        if (!isWeatherDataWorkManagerScheduled()) {
            schedule(
                hour = 11,
                minute = 0,
                intent = Intent(this, AlarmReceiver::class.java)
            )
        }
    }
}
