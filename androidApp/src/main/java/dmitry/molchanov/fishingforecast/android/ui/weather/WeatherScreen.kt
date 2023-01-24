package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.domain.model.ForecastSettingsItem
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.utils.getDayCount
import dmitry.molchanov.domain.utils.getMonthDayCount
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.android.WeatherStatisticViewModel
import dmitry.molchanov.fishingforecast.model.Forecast
import dmitry.molchanov.graph.line.DataPoint
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
