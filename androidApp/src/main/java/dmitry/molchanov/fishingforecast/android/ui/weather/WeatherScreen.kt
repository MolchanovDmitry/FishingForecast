package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.madrapps.plot.line.DataPoint
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.android.WeatherStatisticViewModel
import dmitry.molchanov.fishingforecast.model.*
import dmitry.molchanov.fishingforecast.utils.getDayCount
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WeatherScreen(
    mapPointId: MapPoint,
    forecastSettings: List<ForecastSetting>
) {
    val weatherViewModel by viewModel<WeatherStatisticViewModel> { parametersOf(mapPointId) }
    val state = weatherViewModel.stateFlow.collectAsState()
    val weatherData = state.value.weatherData
    val forecasts = state.value.forecasts
    var positiveCount = 0
    forecasts.forEach { forecast ->
        if (forecast.isGood) positiveCount += 1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Text(text = "Общая оценка = $positiveCount из ${forecasts.size}")

        weatherData.mapNotNull {
            it.temperature?.avg?.let(it::getDataPointByValue)
        }.ifEmpty { null }
            ?.let { avgTemperature ->
                GraphItem(
                    title = stringResource(R.string.temperature_avg),
                    dataPoints = avgTemperature
                )
            }
        forecasts.GetItemForecast(ForecastSettingsItem.TEMPERATURE_AVG)

        weatherData.mapNotNull {
            it.temperature?.water?.let(it::getDataPointByValue)
        }.ifEmpty { null }
            ?.let { waterTemperature ->
                GraphItem(
                    title = stringResource(R.string.temperature_water),
                    dataPoints = waterTemperature
                )
            }
        forecasts.GetItemForecast(ForecastSettingsItem.TEMPERATURE_WATER)

        weatherData.mapNotNull {
            it.pressure?.mm?.let(it::getDataPointByValue)
        }.ifEmpty { null }
            ?.let { pressure ->
                GraphItem(title = stringResource(R.string.pressure_mm), dataPoints = pressure)
            }
        forecasts.GetItemForecast(ForecastSettingsItem.PRESSURE_MM)

        weatherData.mapNotNull {
            it.humidity?.let(it::getDataPointByValue)
        }.ifEmpty { null }
            ?.let { humidity ->
                GraphItem(title = stringResource(R.string.humidity), dataPoints = humidity)
            }
        forecasts.GetItemForecast(ForecastSettingsItem.HUMIDITY)
    }
}

/** Получить точку дата - значение. */
fun WeatherData.getDataPointByValue(value: Float): DataPoint =
    DataPoint(date.getDayCount().toFloat(), value)

@Composable
private fun List<Forecast>.GetItemForecast(forecastSettingsItem: ForecastSettingsItem) {
    firstOrNull { forecast ->
        forecast.forecastSettingsItem == forecastSettingsItem
    }?.let { forecast ->
        Text(text = "Оценка: ${forecast.isGood}")
    }
}