package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import dmitry.molchanov.domain.model.DeltaForecastMark
import dmitry.molchanov.domain.model.ForecastMark
import dmitry.molchanov.domain.model.MaxValueForecastMark
import dmitry.molchanov.domain.model.MinValueForecastMark

@Preview
@Composable
fun GraphItem(
    title: String = "Какой-то показатель",
    dataPoints: List<DataPoint> = defaultDataPoints,
    forecastMars: List<ForecastMark>? = defaultForecastMarks
) {
    //TODO
    if (dataPoints.isEmpty()) return
    val maxBorderPoints = getMaxBorderPoints(dataPoints, forecastMars)
    val minBorderPoints = getMinBorderPoints(dataPoints, forecastMars)
    val maxY = maxBorderPoints?.firstOrNull()?.y ?: Float.MAX_VALUE
    val minY = minBorderPoints?.firstOrNull()?.y ?: Float.MIN_VALUE
    val (topDeltaLine, bottomDeltaLine) =
        getDeltaBorderPoints(dataPoints, forecastMars, minY)
            ?: Pair(null, null)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, modifier = Modifier.align(Alignment.CenterHorizontally))
        LineGraph(
            plot = LinePlot(
                listOfNotNull(
                    topDeltaLine?.let {
                        LinePlot.Line(
                            dataPoints = it,
                            connection = null,
                            intersection = null,
                            areaUnderLine = LinePlot.AreaUnderLine(Color.Green, alpha = 0.2f)
                        )
                    },
                    bottomDeltaLine?.let {
                        LinePlot.Line(
                            dataPoints = it,
                            connection = null,
                            intersection = null,
                            areaUnderLine = LinePlot.AreaUnderLine(Color.White, alpha = 1f)
                        )
                    },
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
    forecastMars: List<ForecastMark>?
): List<DataPoint>? {
    val maxValue =
        (forecastMars?.firstOrNull { it is MaxValueForecastMark } as? MaxValueForecastMark)
            ?.value
            ?: return null
    val startX = dataPoints.minOfOrNull { it.x } ?: return null
    val endX = dataPoints.maxOfOrNull { it.x } ?: return null
    return listOf(
        DataPoint(x = startX, y = maxValue),
        DataPoint(x = endX, y = maxValue)
    )
}

/** Получить линию минимально допустимых значений */
private fun getMinBorderPoints(
    dataPoints: List<DataPoint>,
    forecastMars: List<ForecastMark>?
): List<DataPoint>? {
    val minValue =
        (forecastMars?.firstOrNull { it is MinValueForecastMark } as? MinValueForecastMark)
            ?.value
            ?: return null
    val startX = dataPoints.minOfOrNull { it.x } ?: return null
    val endX = dataPoints.maxOfOrNull { it.x } ?: return null
    return listOf(
        DataPoint(x = startX, y = minValue),
        DataPoint(x = endX, y = minValue)
    )
}

/**
 * Получить верхниюю и нижнию линии дельты показателей.
 */
private fun getDeltaBorderPoints(
    dataPoints: List<DataPoint>,
    forecastMars: List<ForecastMark>?,
    minY: Float
): Pair<List<DataPoint>, List<DataPoint>>? {
    val deltaValue =
        (forecastMars?.firstOrNull { it is DeltaForecastMark } as? DeltaForecastMark)
            ?.value
            ?: return null
    val startX = dataPoints.minOfOrNull { it.x } ?: return null
    val endX = dataPoints.maxOfOrNull { it.x } ?: return null
    val minSuccessY = dataPoints
        .filter { it.y >= minY }
        .minOfOrNull { it.y }
        ?: dataPoints
            .minOfOrNull { it.y }
        ?: return null

    val topLine = listOf(
        DataPoint(x = startX, y = minSuccessY + deltaValue),
        DataPoint(x = endX, y = minSuccessY + deltaValue)
    )
    val bottomLine = listOf(
        DataPoint(x = startX, y = minSuccessY),
        DataPoint(x = endX, y = minSuccessY)
    )
    return topLine to bottomLine
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