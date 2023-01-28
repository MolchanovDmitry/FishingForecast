package dmitry.molchanov.fishingforecast.android.ui.weather

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dmitry.molchanov.domain.model.Forecast
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.ForecastSettingsItem
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.model.WindDir
import dmitry.molchanov.domain.utils.getDayCount
import dmitry.molchanov.domain.utils.getMonthDayCount
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.android.WeatherStatisticViewModel
import dmitry.molchanov.graph.line.DataPoint
import java.util.*
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WeatherScreen(mapPointId: MapPoint, forecastSettings: List<ForecastSetting>) {
    val weatherViewModel = koinViewModel<WeatherStatisticViewModel> { parametersOf(mapPointId) }
    val state = weatherViewModel.stateFlow.collectAsState()
    val weatherData = state.value.weatherData
    val isOnlyOneDigitDays = weatherData.isOnlyOneDigitDays()
    val forecasts = state.value.forecasts
    val context = LocalContext.current
    var positiveCount = 0

    // LaunchedEffect(key1 = Unit) {
    /*weatherViewModel.messageFlow
        .onEach { message -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
        .launchIn(this)*/
    // }

    forecasts.forEach { forecast ->
        if (forecast.isGood) positiveCount += 1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Text(text = "Общая оценка = $positiveCount из ${forecasts.size}")

        DrawWindDir(weatherData)

        weatherData.mapNotNull {
            it.temperature?.avg?.let { value ->
                it.getDataPointByValue(value, shouldMonthInclude = !isOnlyOneDigitDays)
            }
        }.ifEmpty { null }?.let { avgTemperature ->
            GraphItem(
                title = stringResource(R.string.temperature_avg),
                dataPoints = avgTemperature,
                forecastMars = forecastSettings.find { it.forecastSettingsItem == ForecastSettingsItem.TEMPERATURE_AVG }?.forecastMarks
            )
        }
        forecasts.GetItemForecast(ForecastSettingsItem.TEMPERATURE_AVG)

        weatherData.mapNotNull {
            it.temperature?.water?.let { value ->
                it.getDataPointByValue(value, shouldMonthInclude = !isOnlyOneDigitDays)
            }
        }.ifEmpty { null }?.let { waterTemperature ->
            GraphItem(
                title = stringResource(R.string.temperature_water),
                dataPoints = waterTemperature,
                forecastMars = forecastSettings.find { it.forecastSettingsItem == ForecastSettingsItem.TEMPERATURE_WATER }?.forecastMarks
            )
        }
        forecasts.GetItemForecast(ForecastSettingsItem.TEMPERATURE_WATER)

        weatherData.mapNotNull {
            it.pressure?.mm?.let { value ->
                it.getDataPointByValue(value, shouldMonthInclude = !isOnlyOneDigitDays)
            }
        }.ifEmpty { null }?.let { pressure ->
            GraphItem(
                title = stringResource(R.string.pressure_mm),
                dataPoints = pressure,
                forecastMars = forecastSettings.find { it.forecastSettingsItem == ForecastSettingsItem.PRESSURE_MM }?.forecastMarks
            )
        }
        forecasts.GetItemForecast(ForecastSettingsItem.PRESSURE_MM)

        weatherData.mapNotNull {
            it.humidity?.let { value ->
                it.getDataPointByValue(value, shouldMonthInclude = !isOnlyOneDigitDays)
            }
        }.ifEmpty { null }?.let { humidity ->
            GraphItem(
                title = stringResource(R.string.humidity),
                dataPoints = humidity,
                forecastMars = forecastSettings.find { it.forecastSettingsItem == ForecastSettingsItem.HUMIDITY }?.forecastMarks
            )
        }
        forecasts.GetItemForecast(ForecastSettingsItem.HUMIDITY)
    }
}

private fun List<WeatherData>.isOnlyOneDigitDays(): Boolean {
    return this.find { it.date.length() > 1 } != null
}

private fun Long.length(): Int = this.toString().length

/** Получить точку дата - значение. */
fun WeatherData.getDataPointByValue(value: Float, shouldMonthInclude: Boolean): DataPoint {
    val x = if (shouldMonthInclude) date.getMonthDayCount() else date.getDayCount().toFloat()
    return DataPoint(x, value)
}


@Composable
private fun List<Forecast>.GetItemForecast(forecastSettingsItem: ForecastSettingsItem) {
    firstOrNull { forecast ->
        forecast.forecastSettingsItem == forecastSettingsItem
    }?.let { forecast ->
        Text(text = "Оценка: ${forecast.isGood}")
    }
}

@Composable
private fun DrawWindDir(weatherData: List<WeatherData>) {
    val formatter = remember {
        SimpleDateFormat("MM-dd", Locale.getDefault())
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.wind_dir_title), modifier = Modifier.align(CenterHorizontally))
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(weatherData) { weatherItem ->
                Column(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    when (weatherItem.wind?.dir) {
                        WindDir.NW -> 180F + 90 + 45
                        WindDir.N -> 0F
                        WindDir.NE -> 45F
                        WindDir.E -> 90F
                        WindDir.SE -> 90F + 45
                        WindDir.S -> 180F
                        WindDir.SW -> 180F + 45
                        WindDir.W -> 180F + 90
                        WindDir.C -> null
                        null -> null
                    }?.let { angle ->
                        Icon(
                            painter = painterResource(id = R.drawable.ic_double_arrow_up),
                            contentDescription = null,
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .rotate(angle)
                        )
                    }
                    Text(
                        text = formatter.format(weatherItem.date),
                        modifier = Modifier.align(CenterHorizontally)
                    )
                    Text(
                        text = when (weatherItem.wind?.dir) {
                            WindDir.NW -> R.string.wind_dir_nw
                            WindDir.N -> R.string.wind_dir_n
                            WindDir.NE -> R.string.wind_dir_ne
                            WindDir.E -> R.string.wind_dir_e
                            WindDir.SE -> R.string.wind_dir_se
                            WindDir.S -> R.string.wind_dir_s
                            WindDir.SW -> R.string.wind_dir_sw
                            WindDir.W -> R.string.wind_dir_w
                            WindDir.C -> R.string.wind_dir_c
                            null -> null
                        }?.let { strId -> stringResource(strId) }
                            ?: stringResource(R.string.wind_dir_no_data),
                        modifier = Modifier.align(CenterHorizontally)
                    )
                }
            }
        }
    }
}
