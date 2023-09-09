package dmitry.molchanov.fishingforecast.android.ui.weather

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dmitry.molchanov.domain.model.CommonProfile
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.model.WeatherDate
import dmitry.molchanov.domain.utils.DayPart
import dmitry.molchanov.fishingforecast.android.R
import java.util.*

@Preview
@Composable
fun DrawMoonPhase(
    weatherData: List<WeatherData> =
        getPreviewWeatherDataByMoonCodes(moonCodes = (0..30).toList())
) {
    weatherData.find { it.moonCode in 0..15 } ?: return
    val formatter = remember {
        SimpleDateFormat("MM-dd", Locale.getDefault())
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.moon_phase_title),
            modifier = Modifier.align(CenterHorizontally)
        )
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(weatherData) { weatherItem ->
                Column(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    when (weatherItem.moonCode) {
                        0 -> R.drawable.ic_moon_0
                        1 -> R.drawable.ic_moon_1
                        2 -> R.drawable.ic_moon_12
                        3 -> R.drawable.ic_moon_3
                        4 -> R.drawable.ic_moon_4
                        5 -> R.drawable.ic_moon_5
                        6 -> R.drawable.ic_moon_6
                        7 -> R.drawable.ic_moon_7
                        8 -> R.drawable.ic_moon_8
                        9 -> R.drawable.ic_moon_9
                        10 -> R.drawable.ic_moon_10
                        11 -> R.drawable.ic_moon_11
                        12 -> R.drawable.ic_moon_12
                        13 -> R.drawable.ic_moon_13
                        14 -> R.drawable.ic_moon_14
                        15 -> R.drawable.ic_moon_15
                        else -> null
                    }?.let { drawableId ->
                        Icon(
                            painter = painterResource(drawableId),
                            contentDescription = null,
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .width(50.dp)
                                .height(50.dp),
                        )
                    }

                    Text(
                        text = when (weatherItem.moonCode) {
                            0 -> R.string.moon_full
                            in 1..3 -> R.string.moon_waning
                            4 -> R.string.moon_last_quarter
                            in 5..7 -> R.string.moon_waning
                            8 -> R.string.moon_new
                            in 9..11 -> R.string.moon_waxing_crescent
                            12 -> R.string.moon_last_quarter
                            13 - 15 -> R.string.moon_waxing_crescent
                            else -> R.string.moon_unknown
                        }.let { moonPhaseStr -> stringResource(moonPhaseStr) },
                        modifier = Modifier.align(CenterHorizontally)
                    )
                    Text(
                        text = formatter.format(weatherItem.date.raw),
                        modifier = Modifier.align(CenterHorizontally)
                    )
                }
            }
        }
    }
}

private fun getPreviewWeatherDataByMoonCodes(moonCodes: List<Int>) =
    moonCodes.map { moonCode ->
        WeatherData(
            id = 0,
            date = WeatherDate(raw = 5242, year = 1984, month = 6039, day = 5087, dayPart = DayPart.NIGHT),
            mapPoint = MapPoint(
                id = 0,
                name = "",
                profile = CommonProfile(name = ""),
                latitude = 0.0,
                longitude = 0.0,
            ),
            pressure = null,
            temperature = null,
            wind = null,
            humidity = null,
            moonCode = moonCode
        )
    }