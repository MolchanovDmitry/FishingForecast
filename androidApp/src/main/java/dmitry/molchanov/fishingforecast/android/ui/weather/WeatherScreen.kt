package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.runtime.Composable
import com.madrapps.plot.line.DataPoint
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.utils.getDayCount

@Composable
fun WeatherScreen(weatherData: List<WeatherData>) {
    val avgTemperature = weatherData.mapNotNull {
        it.temperature?.avg?.let { avgTemperature ->
            DataPoint(it.date.getDayCount().toFloat(), avgTemperature)
        }
    }
    GraphItem(title = "Средняя теспература", dataPoints = avgTemperature)
}