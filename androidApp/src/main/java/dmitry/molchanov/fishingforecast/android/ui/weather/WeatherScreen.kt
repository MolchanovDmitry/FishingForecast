package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.utils.getDayCount

@Composable
fun WeatherScreen(weatherData: List<WeatherData>) {
    val avgTemperature = weatherData.mapNotNull {
        it.temperature?.avg?.let { avgTemperature ->
            DataPoint(it.date.getDayCount().toFloat(), avgTemperature)
        }
    }
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    avgTemperature,
                    LinePlot.Connection(Color.Blue, 3.dp),
                    LinePlot.Intersection(Color.Green, 6.dp) { center, point ->
                        drawCircle(Color.Green, 6.dp.toPx(), center)
                    },
                    LinePlot.Highlight { center ->
                        val color = Color.Red
                        drawCircle(color, 9.dp.toPx(), center, alpha = 1f)
                        drawCircle(color, 6.dp.toPx(), center)
                        drawCircle(color, 3.dp.toPx(), center)
                    },
                ),
            ), LinePlot.Grid(Color.Gray), paddingRight = 16.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}