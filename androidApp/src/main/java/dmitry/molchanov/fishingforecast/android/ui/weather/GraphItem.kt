package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import dmitry.molchanov.fishingforecast.model.DeltaForecastMark
import dmitry.molchanov.fishingforecast.model.ForecastMark
import dmitry.molchanov.fishingforecast.model.MaxValueForecastMark
import dmitry.molchanov.fishingforecast.model.MinValueForecastMark

@Preview
@Composable
fun GraphItem(
    title: String = "Какой-то показатель",
    dataPoints: List<DataPoint> = defaultDataPoints,
    forecastMars: List<ForecastMark> = defaultForecastMarks
) {
    val maxBorderPoints = getMaxBorderPoints(dataPoints, forecastMars)
    val minBorderPoints = getMinBorderPoints(dataPoints, forecastMars)
    val maxY = maxBorderPoints?.firstOrNull()?.y ?: Float.MAX_VALUE
    val minY = minBorderPoints?.firstOrNull()?.y ?: Float.MIN_VALUE
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title)
        LineGraph(
            plot = LinePlot(
                listOfNotNull(
                    getMaxBorderPoints(dataPoints, forecastMars)?.let {
                        LinePlot.Line(
                            dataPoints = it,
                            connection = LinePlot.Connection(Color.Red, 2.dp),
                            intersection = null,
                        )
                    },
                    getMinBorderPoints(dataPoints, forecastMars)?.let {
                        LinePlot.Line(
                            dataPoints = it,
                            connection = LinePlot.Connection(Color.Red, 2.dp),
                            intersection = null,
                        )
                    },
                    LinePlot.Line(
                        dataPoints = dataPoints,
                        connection = LinePlot.Connection(Color.Blue, 3.dp),
                        intersection = LinePlot.Intersection(Color.Green, 6.dp) { center, point ->
                            if (point.y > maxY || point.y < minY) {
                                drawCircle(Color.Red, 6.dp.toPx(), center)
                            } else {
                                drawCircle(Color.Green, 5.dp.toPx(), center)
                            }
                        },
                    ),
                ), LinePlot.Grid(Color.Gray), paddingRight = 16.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

/** Получить линию максимально допустимых значений */
private fun getMaxBorderPoints(
    dataPoints: List<DataPoint>,
    forecastMars: List<ForecastMark>
): List<DataPoint>? {
    val maxValue =
        (forecastMars.firstOrNull { it is MaxValueForecastMark } as? MaxValueForecastMark)
            ?.value
            ?: return null
    val startX = dataPoints.minOf { it.x }
    val endX = dataPoints.maxOf { it.x }
    return listOf(
        DataPoint(x = startX, y = maxValue),
        DataPoint(x = endX, y = maxValue)
    )
}

/** Получить линию минимально допустимых значений */
private fun getMinBorderPoints(
    dataPoints: List<DataPoint>,
    forecastMars: List<ForecastMark>
): List<DataPoint>? {
    val minValue =
        (forecastMars.firstOrNull { it is MinValueForecastMark } as? MinValueForecastMark)
            ?.value
            ?: return null
    val startX = dataPoints.minOf { it.x }
    val endX = dataPoints.maxOf { it.x }
    return listOf(
        DataPoint(x = startX, y = minValue),
        DataPoint(x = endX, y = minValue)
    )
}

val defaultDataPoints = listOf(
    DataPoint(x = 1f, y = 9f),
    DataPoint(x = 2f, y = 15f),
    DataPoint(x = 3f, y = 20f),
    DataPoint(x = 4f, y = 25f),
    DataPoint(x = 5f, y = 30f),
    DataPoint(x = 6f, y = 40f),
    DataPoint(x = 7f, y = 0f),
)

val defaultForecastMarks = listOf(
    MinValueForecastMark(10f),
    MaxValueForecastMark(30f),
    DeltaForecastMark(10f)
)