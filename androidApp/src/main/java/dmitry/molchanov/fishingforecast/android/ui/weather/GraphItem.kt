package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dmitry.molchanov.domain.model.DeltaForecastMark
import dmitry.molchanov.domain.model.ForecastMark
import dmitry.molchanov.domain.model.MaxValueForecastMark
import dmitry.molchanov.domain.model.MinValueForecastMark
import dmitry.molchanov.graph.line.DataPoint
import dmitry.molchanov.graph.line.LineGraph
import dmitry.molchanov.graph.line.LinePlot
import java.text.DecimalFormat

@Preview
@Composable
fun GraphItem(
    title: String = "Какой-то показатель",
    dataPoints: List<DataPoint> = defaultDataPoints,
    forecastMars: List<ForecastMark>? = defaultForecastMarks
) {
    // TODO
    if (dataPoints.isEmpty()) return
    val maxBorderPoints = getMaxBorderPoints(dataPoints, forecastMars)
    val minBorderPoints = getMinBorderPoints(dataPoints, forecastMars)
    val maxY = maxBorderPoints?.firstOrNull()?.y ?: Float.MAX_VALUE
    val minY = minBorderPoints?.firstOrNull()?.y ?: Float.MIN_VALUE
    val xOffset = remember { mutableStateOf(0f) }
    val yOffset = remember { mutableStateOf(0f) }
    val cardWidth = remember { mutableStateOf(0) }
    val visibility = remember { mutableStateOf(false) }
    val points = remember { mutableStateOf(listOf<DataPoint>()) }
    var parentSize by remember { mutableStateOf(IntSize(0, 0)) }
    val density = LocalDensity.current
    val padding = 16.dp
    val (topDeltaLine, bottomDeltaLine) = getDeltaBorderPoints(dataPoints, forecastMars, minY)
        ?: Pair(null, null)

    Box(modifier = Modifier
        .fillMaxWidth()
        .onSizeChanged {
            parentSize = it
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
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

                            intersection = LinePlot.Intersection(
                                Color.Green,
                                6.dp
                            ) { center, point ->
                                if (point.y > maxY || point.y < minY) {
                                    drawCircle(Color.Red, 6.dp.toPx(), center)
                                } else {
                                    drawCircle(Color.Green, 5.dp.toPx(), center)
                                }
                            },
                        ),
                    ),
                    LinePlot.Grid(Color.LightGray),
                    paddingRight = 16.dp,
                ),
                onSelectionStart = { visibility.value = true },
                onSelectionEnd = { visibility.value = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) { x, pts ->
                val cWidth = cardWidth.value.toFloat()
                var xCenter = x + padding.toPx(density)
                xCenter = when {
                    xCenter + cWidth / 2f > parentSize.width -> parentSize.width - cWidth
                    xCenter - cWidth / 2f < 0f -> 0f
                    else -> xCenter - cWidth / 2f
                }
                xOffset.value = xCenter
                points.value = pts
            }
        }
        Box(Modifier.height(150.dp)) {
            if (visibility.value) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .onGloballyPositioned {
                            cardWidth.value = it.size.width
                        }
                        .graphicsLayer(translationX = xOffset.value, translationY = 0F),
                    shape = RoundRectangle,
                    color = Color.Gray
                ) {
                    Column(
                        Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        val value = points.value
                        if (value.isNotEmpty()) {
                            val format = DecimalFormat("#.##")
                            val x = format.format(value[0].x)
                            val y = format.format(value[0].y)
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = "Число: $x, значение: $y",
                                style = MaterialTheme.typography.subtitle1,
                                color = Color.White
                            )
                        }
                    }
                }
            }

        }
    }
}

private fun Dp.toPx(density: Density) = value * density.density

/** Получить линию максимально допустимых значений */
private fun getMaxBorderPoints(
    dataPoints: List<DataPoint>, forecastMars: List<ForecastMark>?
): List<DataPoint>? {
    val maxValue =
        (forecastMars?.firstOrNull { it is MaxValueForecastMark } as? MaxValueForecastMark)?.value
            ?: return null
    val startX = dataPoints.minOfOrNull { it.x } ?: return null
    val endX = dataPoints.maxOfOrNull { it.x } ?: return null
    return listOf(
        DataPoint(x = startX, y = maxValue), DataPoint(x = endX, y = maxValue)
    )
}

/** Получить линию минимально допустимых значений */
private fun getMinBorderPoints(
    dataPoints: List<DataPoint>, forecastMars: List<ForecastMark>?
): List<DataPoint>? {
    val minValue =
        (forecastMars?.firstOrNull { it is MinValueForecastMark } as? MinValueForecastMark)?.value
            ?: return null
    val startX = dataPoints.minOfOrNull { it.x } ?: return null
    val endX = dataPoints.maxOfOrNull { it.x } ?: return null
    return listOf(
        DataPoint(x = startX, y = minValue), DataPoint(x = endX, y = minValue)
    )
}

/**
 * Получить верхниюю и нижнию линии дельты показателей.
 */
private fun getDeltaBorderPoints(
    dataPoints: List<DataPoint>, forecastMars: List<ForecastMark>?, minY: Float
): Pair<List<DataPoint>, List<DataPoint>>? {
    val deltaValue =
        (forecastMars?.firstOrNull { it is DeltaForecastMark } as? DeltaForecastMark)?.value
            ?: return null
    val startX = dataPoints.minOfOrNull { it.x } ?: return null
    val endX = dataPoints.maxOfOrNull { it.x } ?: return null
    val minSuccessY =
        dataPoints.filter { it.y >= minY }.minOfOrNull { it.y } ?: dataPoints.minOfOrNull { it.y }
        ?: return null

    val topLine = listOf(
        DataPoint(x = startX, y = minSuccessY + deltaValue),
        DataPoint(x = endX, y = minSuccessY + deltaValue)
    )
    val bottomLine = listOf(
        DataPoint(x = startX, y = minSuccessY), DataPoint(x = endX, y = minSuccessY)
    )
    return topLine to bottomLine
}

private val RoundRectangle: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline.Rounded {
        val radius = 8.dp.value * density.density
        return Outline.Rounded(RoundRect(size.toRect(), CornerRadius(radius, radius)))
    }

    override fun toString(): String = "RoundRectangleShape"
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
    MinValueForecastMark(10f), MaxValueForecastMark(30f), DeltaForecastMark(10f)
)