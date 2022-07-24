package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import dmitry.molchanov.fishingforecast.android.ui.preview.previewResult
import dmitry.molchanov.fishingforecast.android.ui.preview.previewWeatherData
import dmitry.molchanov.fishingforecast.model.Result
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.utils.getDayCount
import org.koin.androidx.compose.viewModel

@Preview
@Composable
fun ResultDetailScreen(
    result: Result = previewResult,
    weatherData: List<WeatherData> = previewWeatherData,
) {
    val vm by viewModel<ResultDetailViewModel>()
    val state = vm.stateFlow.collectAsState()
    val singapore = LatLng(result.mapPoint.latitude, result.mapPoint.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 12f)
    }
    val sortedWeatherData = remember {
        val sorted = weatherData.sortedBy { it.date }
        sorted.lastOrNull()
            ?.let { vm.onAction(OnDateSelected(it.date)) }
        sorted
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    position = cameraPositionState.position.target,
                    title = "Тверь",
                    snippet = "Какой-то текст",
                )
            }
            Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)) {
                sortedWeatherData.forEach { weatherDataItem ->
                    Text(
                        text = weatherDataItem.date.getDayCount().toString(),
                        color = Color.White,
                        modifier = Modifier.padding(8.dp).drawBehind {
                            drawCircle(
                                alpha = if (weatherDataItem.date == state.value.selectedDate) 1F else 0.4F,
                                color = Color.Blue,
                                radius = this.size.height / 1.4F,
                            )
                        }.clickable {
                            vm.onAction(OnDateSelected(weatherDataItem.date))
                        }
                    )
                }
            }
        }
        Text(modifier = Modifier.fillMaxWidth(), text = "asdasdad")
    }

}