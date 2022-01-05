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
    forecastSettingsItem: ForecastSettingsItem? = ForecastSettingsItem.WIND_SPEED,
    values: List<ForecastSetting> = getPreviewValues(ForecastSettingsItem.WIND_SPEED),
    availableMarksConformity: List<ForecastSettingItemToMarkConformity> =
        forecastSettingItemToMarkConformity,
) {
    var activeForecastSettingsItem by remember {
        mutableStateOf(forecastSettingsItem ?: values.first().forecastSettingsItem)
    }
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
        },
        text = {
            Column {
                DropDown(
                    modifier = Modifier.fillMaxWidth(),
                    label = "some label",
                    suggestions = ForecastSettingsItem.values().map { it.name },
                    defaultSelectedIndex = 0,
                    onSelectIndex = { index ->
                        activeForecastSettingsItem = ForecastSettingsItem.values()[index]
                    }
                )
                availableMarksConformity
                    .filter { it.forecastSettingsItem == activeForecastSettingsItem }
                    .forEach {

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
                        .clickable { }
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
){
    Row(modifier = Modifier.fillMaxWidth()){
        when(forecastMark){

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