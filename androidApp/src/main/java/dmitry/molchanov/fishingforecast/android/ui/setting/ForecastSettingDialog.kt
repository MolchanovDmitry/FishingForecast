package dmitry.molchanov.fishingforecast.android.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmitry.molchanov.fishingforecast.android.ui.DropDown
import dmitry.molchanov.fishingforecast.model.*

@Preview
@Composable
fun ForecastSettingDialog(
    alreadySelectedForecastSettingsItem: List<ForecastSettingsItem> = listOf(ForecastSettingsItem.WIND_SPEED),
    onDismiss: () -> Unit = {}
) {
    val notSelectedItemToMarks = forecastSettingItemToMarkConformity
        .filter { !alreadySelectedForecastSettingsItem.contains(it.forecastSettingsItem) }
    var activeForecastSettingsItem by remember {
        mutableStateOf(notSelectedItemToMarks.first().forecastSettingsItem)
    }
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onDismiss()
        },
        text = {
            Column {
                DropDown(
                    modifier = Modifier.fillMaxWidth(),
                    label = "some label",
                    suggestions = notSelectedItemToMarks.map { it.forecastSettingsItem.name },
                    defaultSelectedIndex = 0,
                    onSelectIndex = { index ->
                        activeForecastSettingsItem =
                            notSelectedItemToMarks[index].forecastSettingsItem
                    }
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    forecastSettingItemToMarkConformity
                        .filter { it.forecastSettingsItem == activeForecastSettingsItem }
                        .flatMap { it.forecastMarkTypes }
                        .forEach { forecastMarkClass ->
                            when (forecastMarkClass) {
                                MinValueForecastMark::class ->
                                    SettingItem(title = "min:", value = -1f)
                                MaxValueForecastMark::class ->
                                    SettingItem(title = "max:", value = -1f)
                                DeltaForecastMark::class ->
                                    SettingItem(title = "delta:", value = -1f)
                                ExactValueForecastMark::class ->
                                    SettingItem(title = "значение", value = -1f)
                            }
                        }
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "Отменить",
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onDismiss()
                        }
                )
                Text(
                    "Сохранить",
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                        }
                )
            }
        }
    )
}


@Preview
@Composable
private fun ForecastSettingView(
    forecastMark: ForecastMark = MaxValueForecastMark(99F)
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        when (forecastMark) {

        }
    }
}

@Suppress("SameParameterValue")
private fun getPreviewValues(forecastSettingsItem: ForecastSettingsItem): List<ForecastSetting> =
    listOf(
        ForecastSetting(
            forecastSettingsItem = forecastSettingsItem,
            forecastMarks = listOf(
                MinValueForecastMark(1F),
                MaxValueForecastMark(2.5F),
                DeltaForecastMark(100F)
            )
        )
    )