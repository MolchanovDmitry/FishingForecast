package dmitry.molchanov.fishingforecast.android.ui.weather

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.model.WindDir
import dmitry.molchanov.fishingforecast.android.R
import java.util.*

@Composable
fun DrawWindDir(weatherData: List<WeatherData>) {
    val formatter = remember {
        SimpleDateFormat("MM-dd", Locale.getDefault())
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.wind_dir_title),
            modifier = Modifier.align(CenterHorizontally)
        )
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(weatherData) { weatherItem ->
                Column(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    when (weatherItem.wind?.dir) {
                        WindDir.NW -> 90F + 45
                        WindDir.N -> 180F
                        WindDir.NE -> 180F + 45F
                        WindDir.E -> 180F + 90F
                        WindDir.SE -> 180F + 90F + 45
                        WindDir.S -> 0F
                        WindDir.SW -> 45F
                        WindDir.W -> 90F
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
                    Text(
                        text = formatter.format(weatherItem.date),
                        modifier = Modifier.align(CenterHorizontally)
                    )
                }
            }
        }
    }
}
