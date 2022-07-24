package dmitry.molchanov.fishingforecast.android.ui.result

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
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
import java.util.*

private val UNKNOWN = "Нет данных"

@Preview
@Composable
fun ResultDetailScreen(
    result: Result = previewResult,
    weatherData: List<WeatherData> = previewWeatherData,
) {
    val vm by viewModel<ResultDetailViewModel>()
    val state = vm.stateFlow.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        val point = LatLng(result.mapPoint.latitude, result.mapPoint.longitude)
        position = CameraPosition.fromLatLngZoom(point, 12f)
    }
    val sortedWeatherData = remember {
        val sorted = weatherData.sortedBy { it.date }
        sorted.lastOrNull()
            ?.let { vm.onAction(OnDateSelected(it.date)) }
        sorted
    }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
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
        weatherData.firstOrNull {
            it.date == state.value.selectedDate
        }?.let { selectedWeatherDataItem -> ResultDetailColumn(selectedWeatherDataItem) }

    }

}

@Composable
private fun ResultDetailColumn(weatherDateItem: WeatherData) {
    val simpleDateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }
    val dataStr = simpleDateFormat.format(weatherDateItem.date)

    ResultDetailItemRow(title = "Дата", value = dataStr)

    ResultDetailItemRow(
        title = "Средняя температура окружающей среды",
        value = weatherDateItem.temperature?.avg?.toString() ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Минимальная температура окражающей среды",
        value = weatherDateItem.temperature?.min?.toString() ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Максимальная температура окружающей среды",
        value = weatherDateItem.temperature?.max?.toString() ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Температура воды",
        value = weatherDateItem.temperature?.water?.toString() ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Влажность",
        value = weatherDateItem.humidity?.toString() ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Давление в мм ртутного столба",
        value = weatherDateItem.pressure?.mm?.toString() ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Давление в паскалях",
        value = weatherDateItem.pressure?.pa?.toString() ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Направление ветра",
        value = weatherDateItem.wind?.dir ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Порыв ветра",
        value = weatherDateItem.wind?.gust?.toString() ?: UNKNOWN
    )
    ResultDetailItemRow(
        title = "Скорость ветра",
        value = weatherDateItem.wind?.speed?.toString() ?: UNKNOWN
    )

}

@Composable
private fun ResultDetailItemRow(title: String, value: String, showDivider: Boolean = true) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value)
    }
    if (showDivider) {
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
    }
}