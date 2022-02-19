package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel

@Composable
fun WeatherDebugScreen() {
    val vm = getViewModel<WeatherDebugViewModel>()
    Row(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Получить данные с сервера")
        }
    }
}