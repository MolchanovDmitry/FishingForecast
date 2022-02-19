package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.madrapps.plot.line.DataPoint
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.model.ForecastSetting
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.utils.getDayCount

@Composable
fun WeatherScreen(weatherData: List<WeatherData>, forecastSettings: List<ForecastSetting>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        weatherData.mapNotNull {
            it.temperature?.avg?.let(it::getDataPointByValue)
        }.ifEmpty { null }
            ?.let { avgTemperature ->
                GraphItem(
                    title = stringResource(R.string.temperature_avg),
                    dataPoints = avgTemperature
                )
            }

        weatherData.mapNotNull {
            it.temperature?.water?.let(it::getDataPointByValue)
        }.ifEmpty { null }
            ?.let { waterTemperature ->
                GraphItem(
                    title = stringResource(R.string.temperature_water),
                    dataPoints = waterTemperature
                )
            }

        weatherData.mapNotNull {
            it.pressure?.mm?.let(it::getDataPointByValue)
        }.ifEmpty { null }
            ?.let { pressure ->
                GraphItem(title = stringResource(R.string.pressure_mm), dataPoints = pressure)
            }

        weatherData.mapNotNull {
            it.humidity?.let(it::getDataPointByValue)
        }.ifEmpty { null }
            ?.let { humidity ->
                GraphItem(title = stringResource(R.string.humidity), dataPoints = humidity)
            }
    }
}

/** Получить точку дата - значение. */
fun WeatherData.getDataPointByValue(value: Float): DataPoint =
    DataPoint(date.getDayCount().toFloat(), value)