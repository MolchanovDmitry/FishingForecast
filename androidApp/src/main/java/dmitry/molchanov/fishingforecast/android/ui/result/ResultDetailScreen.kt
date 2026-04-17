package dmitry.molchanov.fishingforecast.android.ui.result

import android.icu.text.SimpleDateFormat
import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.ui_view.ViewProvider
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.model.WindDir
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.android.ui.common.rememberMapViewWithLifecycle
import dmitry.molchanov.fishingforecast.android.ui.preview.previewResult
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Preview
@Composable
fun ResultDetailScreen(result: Result = previewResult) {
    val vm = koinViewModel<ResultDetailViewModel> { parametersOf(result) }
    val state = vm.stateFlow.collectAsState()
    val weatherData = state.value.weatherData
    val mapView = rememberMapViewWithLifecycle()
    val sortedWeatherData = state.value.weatherData.sortedBy { it.date.roundedValue }
    if (sortedWeatherData.isNotEmpty()) {
        LaunchedEffect(Unit) {
            sortedWeatherData.lastOrNull()?.let { vm.onAction(OnDateSelected(it.date)) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Детали — ${result.name}") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            /*GoogleMap(
                modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState
            ) {
                Marker(
                    position = cameraPositionState.position.target,
                    title = "Тверь",
                    snippet = "Какой-то текст",
                )
            }*/
            AndroidView(factory = {
                mapView.apply {
                    mapView.map.isZoomGesturesEnabled = true
                    mapView.map.mapObjects.addPlacemark(
                        Point(
                            result.mapPoint.latitude,
                            result.mapPoint.longitude
                        ),
                        ViewProvider(
                            TextView(context).apply {
                                text = result.name
                            },
                            false
                        )
                    )
                    mapView.map.move(
                        CameraPosition(
                            Point(
                                result.mapPoint.latitude,
                                result.mapPoint.longitude
                            ),
                            12.0f,
                            0.0f,
                            0.0f
                        )
                    )
                }
            })
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            ) {
                sortedWeatherData.forEach { weatherDataItem ->
                    val isSelected = weatherDataItem.date == state.value.selectedDate
                    val isEmpty = weatherDataItem.id < 0
                    val isResultDay = !isEmpty && weatherDataItem.date.roundedValue == state.value.selectedDate?.roundedValue

                    Text(
                        text = weatherDataItem.date.day.toString(),
                        color = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .drawBehind {
                                val radius = this.size.height / 1.4F
                                val fillColor = when {
                                    isResultDay -> Color.Green
                                    isEmpty -> Color.Gray
                                    else -> Color.Blue
                                }
                                drawCircle(color = fillColor, radius = radius)
                                if (isResultDay) {
                                    drawCircle(
                                        color = Color.White,
                                        radius = radius,
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                                    )
                                }
                            }
                            .clickable {
                                if (!isEmpty) {
                                    vm.onAction(OnDateSelected(weatherDataItem.date))
                                }
                            }
                    )
                }
            }
        }
        weatherData.find { it.date == state.value.selectedDate }
            ?.let { selectedWeatherDataItem ->
                // Описание результата
                result.description?.let { desc ->
                    if (desc.isNotBlank()) {
                        ResultDetailItemRow(
                            title = stringResource(R.string.description_label),
                            value = desc
                        )
                    }
                }
                ResultDetailColumn(selectedWeatherDataItem)
            }
        }
    }
}

@Composable
private fun ResultDetailColumn(weatherDateItem: WeatherData) {
    val simpleDateFormat =
        remember { SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault()) }
    val dataStr = simpleDateFormat.format(weatherDateItem.date.roundedValue)

    // Пустая ячейка — нет данных
    if (weatherDateItem.id < 0) {
        ResultDetailItemRow(title = stringResource(R.string.date), value = "$dataStr — ${stringResource(R.string.no_data)}", showDivider = false)
        return
    }

    ResultDetailItemRow(title = stringResource(R.string.date), value = dataStr)

    weatherDateItem.temperature?.avg?.toString()?.let { tempAvgValue ->
        ResultDetailItemRow(stringResource(R.string.value_temp_avg), tempAvgValue)
    }
    weatherDateItem.temperature?.min?.toString()?.let { tempMinValue ->
        ResultDetailItemRow(stringResource(R.string.value_temp_min), tempMinValue)
    }
    weatherDateItem.temperature?.max?.toString()?.let { temMaxValue ->
        ResultDetailItemRow(stringResource(R.string.value_temp_max), temMaxValue)
    }
    weatherDateItem.temperature?.water?.toString()?.let { tempWaterValue ->
        ResultDetailItemRow(stringResource(R.string.value_temp_water), tempWaterValue)
    }
    weatherDateItem.humidity?.toString()?.let { humidity ->
        ResultDetailItemRow(stringResource(R.string.value_humidity), humidity)
    }
    weatherDateItem.pressure?.mm?.toString()?.let { pressureMm ->
        ResultDetailItemRow(stringResource(R.string.value_pres_mm), pressureMm)
    }
    weatherDateItem.pressure?.pa?.toString()?.let { pa ->
        ResultDetailItemRow(stringResource(R.string.value_pres_pa), pa)
    }
    weatherDateItem.wind?.dir?.let { dir ->
        ResultDetailItemRow(
            stringResource(R.string.value_wind_dir),
            when (dir) {
                WindDir.NW -> R.string.wind_dir_nw
                WindDir.N -> R.string.wind_dir_n
                WindDir.NE -> R.string.wind_dir_ne
                WindDir.E -> R.string.wind_dir_e
                WindDir.SE -> R.string.wind_dir_se
                WindDir.S -> R.string.wind_dir_s
                WindDir.SW -> R.string.wind_dir_sw
                WindDir.W -> R.string.wind_dir_w
                WindDir.C -> R.string.wind_dir_c
            }.let { strId -> stringResource(strId) }
        )
    }
    weatherDateItem.wind?.gust?.toString()?.let { gust ->
        ResultDetailItemRow(stringResource(R.string.value_wind_gust), gust)
    }
    weatherDateItem.wind?.speed?.toString()?.let { speed ->
        ResultDetailItemRow(stringResource(R.string.value_wind_speed), speed, showDivider = false)
    }
}

@Composable
private fun ResultDetailItemRow(title: String, value: String, showDivider: Boolean = true) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value)
    }
    if (showDivider) {
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
