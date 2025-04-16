package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.utils.string
import dmitry.molchanov.fishingforecast.android.FetchWeatherData
import dmitry.molchanov.fishingforecast.android.MainViewModel

@Composable
fun WeatherDebugScreen(vm: MainViewModel, onMapPointSelected: (MapPoint) -> Unit) {
    val state = vm.state.collectAsState()
    val lastDate = state.value.weatherData.maxOfOrNull { it.date.roundedValue }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Дата последнего запроса: ${lastDate?.string() ?: "не запрашивались"}")
        Button(modifier = Modifier.fillMaxWidth(), onClick = { vm.onEvent(FetchWeatherData) }) {
            Text("Получить данные")
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.value.weatherData.map { it.mapPoint }.distinct()) { item ->
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            state.value.weatherData
                                .firstOrNull { it.mapPoint.name == item.name && it.mapPoint.profile == item.profile }
                                ?.mapPoint
                                ?.let { mapPoint -> onMapPointSelected(mapPoint) }
                        }
                )
                Divider(color = Color.LightGray, modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}
