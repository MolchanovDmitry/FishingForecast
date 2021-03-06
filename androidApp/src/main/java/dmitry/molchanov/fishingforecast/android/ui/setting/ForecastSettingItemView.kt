package dmitry.molchanov.fishingforecast.android.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dmitry.molchanov.fishingforecast.android.mapper.toString
import dmitry.molchanov.fishingforecast.model.*

@Preview
@Composable
fun ForecastSettingItemView(
    forecastSettingItem: ForecastSetting = previewForecastSetting,
    onDeletePressed: (ForecastSetting) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(8.dp)
            ) {
                Text(
                    text = forecastSettingItem.forecastSettingsItem.toString(LocalContext.current),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    forecastSettingItem.forecastMarks.forEach { forecastMark ->
                        when (forecastMark) {
                            is MinValueForecastMark ->
                                SettingItem(title = "min:", value = forecastMark.value)
                            is MaxValueForecastMark ->
                                SettingItem(title = "max:", value = forecastMark.value)
                            is DeltaForecastMark ->
                                SettingItem(title = "delta:", value = forecastMark.value)
                            is ExactValueForecastMark ->
                                SettingItem(title = "????????????????", value = forecastMark.value)
                        }
                    }
                }
            }
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(45.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        onDeletePressed(forecastSettingItem)
                    }
            )
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun RowScope.SettingItem(title: String, value: Float? = null) {
    OutlinedTextField(
        value = value.toString(),
        label = { Text(title) },
        modifier = Modifier
            .padding(8.dp)
            .weight(1F),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { inputValue ->
            //inputValue.toIntOrNull()?.let { startPosition = it }
        })
}

private val previewForecastSetting =
    ForecastSetting(
        forecastSettingsItem = ForecastSettingsItem.TEMPERATURE_MAX,
        forecastMarks = listOf(
            MinValueForecastMark(value = 15F),
            MaxValueForecastMark(value = 30F),
            DeltaForecastMark(value = 5F)
        )
    )