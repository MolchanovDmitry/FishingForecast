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
import androidx.compose.ui.unit.dp
import dmitry.molchanov.fishingforecast.android.DeleteForecastSetting
import dmitry.molchanov.fishingforecast.android.MainViewModel
import dmitry.molchanov.fishingforecast.android.SaveForecastSettingMark
import dmitry.molchanov.fishingforecast.model.ForecastSetting

@Composable
fun ForecastSettingsList(
    vm: MainViewModel,
) {
    val state = vm.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.value.forecastSettings) { item ->
                ForecastSettingItemView(item) { forecastSetting ->
                    vm.onEvent(DeleteForecastSetting(forecastSetting))
                }
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
            ForecastSettingDialog(
                onDismiss = {
                    showDialog = false
                },
                onSuccess = {
                    showDialog = false
                    vm.onEvent(
                        SaveForecastSettingMark(
                            ForecastSetting(
                                forecastMarks = it.second,
                                forecastSettingsItem = it.first,
                            )
                        )
                    )
                }
            )
        }
    }
}