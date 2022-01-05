package dmitry.molchanov.fishingforecast.android.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dmitry.molchanov.fishingforecast.model.*

@Preview
@Composable
fun ForecastSettingsList(
    forecastSettings: List<ForecastSetting> = previewForecastSetting
) {
    var showDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(forecastSettings) { item ->
                ForecastSettingItemView(item)
            }
        }
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(90.dp)
                .padding(16.dp)
                .clickable {
                    showDialog = true
                }
        )
        if (showDialog) {
            ForecastSettingDialog {
                showDialog = false
            }
        }
    }
}

private val previewForecastSetting = listOf(
    ForecastSetting(
        forecastSettingsItem = ForecastSettingsItem.TEMPERATURE_MAX,
        forecastMarks = listOf(
            MinValueForecastMark(value = 15F),
            MaxValueForecastMark(value = 30F),
            DeltaForecastMark(value = 5F)
        )
    ),
    ForecastSetting(
        forecastSettingsItem = ForecastSettingsItem.WIND_SPEED,
        forecastMarks = listOf(
            MinValueForecastMark(value = 1F),
            MaxValueForecastMark(value = 7F),
            DeltaForecastMark(value = 1F)
        )
    )
)