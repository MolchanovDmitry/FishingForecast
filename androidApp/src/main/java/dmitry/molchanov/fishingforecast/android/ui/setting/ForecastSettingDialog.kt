package dmitry.molchanov.fishingforecast.android.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmitry.molchanov.fishingforecast.android.ui.DropDown
import dmitry.molchanov.fishingforecast.model.*

@Preview
@Composable
fun ForecastSettingDialog(
    alreadySelectedForecastSettingsItem: List<ForecastSettingsItem> = listOf(ForecastSettingsItem.WIND_SPEED),
    onDismiss: () -> Unit = {},
    onSuccess: (Pair<ForecastSettingsItem, List<ForecastMark>>) -> Unit = {}
) {
    val notSelectedItemToMarks = forecastSettingItemToMarkConformity
        .filter { !alreadySelectedForecastSettingsItem.contains(it.forecastSettingsItem) }
    val selectedForecastMarks by remember { mutableStateOf(mutableListOf<ForecastMark>()) }
    var activeForecastSettingsItem by remember {
        mutableStateOf(notSelectedItemToMarks.first().forecastSettingsItem)
    }
    var shouldAddHint by remember { mutableStateOf(false) }
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
                    selectedForecastMarks.clear()
                    forecastSettingItemToMarkConformity
                        .firstOrNull { it.forecastSettingsItem == activeForecastSettingsItem }
                        ?.forecastMarkTypes
                        ?.forEach { forecastMarkClass ->
                            when (forecastMarkClass) {
                                MinValueForecastMark::class ->
                                    SettingItem(title = "min:") { minValue ->
                                        shouldAddHint = false
                                        MinValueForecastMark(minValue)
                                            .let(selectedForecastMarks::replaceOrAdd)
                                    }
                                MaxValueForecastMark::class ->
                                    SettingItem(title = "max:") { maxValue ->
                                        shouldAddHint = false
                                        MaxValueForecastMark(maxValue)
                                            .let(selectedForecastMarks::replaceOrAdd)
                                    }
                                DeltaForecastMark::class ->
                                    SettingItem(title = "delta:") { delta ->
                                        shouldAddHint = false
                                        DeltaForecastMark(delta)
                                            .let(selectedForecastMarks::replaceOrAdd)
                                    }
                                ExactValueForecastMark::class ->
                                    SettingItem(title = "значение") { value ->
                                        shouldAddHint = false
                                        ExactValueForecastMark(value)
                                            .let(selectedForecastMarks::replaceOrAdd)
                                    }
                            }
                        }
                }
                if (shouldAddHint) {
                    Text(
                        text = "Заполните хотя бы одно значение", color = Color.Red,
                    )
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
                            if (selectedForecastMarks.isEmpty()) {
                                shouldAddHint = true
                            } else {
                                onSuccess(activeForecastSettingsItem to selectedForecastMarks)
                            }
                        }
                )
            }
        }
    )
}

@Composable
private fun RowScope.SettingItem(title: String, value: Float? = null, onChange: (Float) -> Unit) {
    var result by remember { mutableStateOf(value?.toString()) }
    OutlinedTextField(
        value = result ?: "",
        label = { Text(title) },
        modifier = Modifier
            .padding(8.dp)
            .weight(1F),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = { inputValue ->
            result = inputValue
            inputValue.toFloatOrNull()?.let { onChange(it) }
        })
}

private fun MutableList<ForecastMark>.replaceOrAdd(forecastMark: ForecastMark){
    firstOrNull { it::class == forecastMark::class }
        ?.let(::remove)
    add(forecastMark)
}