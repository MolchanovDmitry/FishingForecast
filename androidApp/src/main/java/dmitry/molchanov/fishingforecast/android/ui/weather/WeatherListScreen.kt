package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import dmitry.molchanov.fishingforecast.android.FetchWeatherData
import dmitry.molchanov.fishingforecast.android.MainViewModel
import dmitry.molchanov.fishingforecast.model.MapPoint

@Composable
fun WeatherDebugScreen(vm: MainViewModel, onMapPointSelected: (MapPoint) -> Unit) {
    val state = vm.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Button(modifier = Modifier.fillMaxWidth(), onClick = { vm.onEvent(FetchWeatherData) }) {
            Text("Получить данные")
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.value.weatherData.map { it.mapPoint }.distinct()) { item ->
                Text(text = item.name, modifier = Modifier.clickable {
                    state.value.weatherData
                        .firstOrNull { it.mapPoint.name == item.name && it.mapPoint.profileName == item.profileName }
                        ?.mapPoint
                        ?.let { mapPoint -> onMapPointSelected(mapPoint) }
                })
            }
        }
    }

}