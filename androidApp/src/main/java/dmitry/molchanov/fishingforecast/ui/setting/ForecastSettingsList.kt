package dmitry.molchanov.fishingforecast.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmitry.molchanov.domain.model.ForecastSetting
import dmitry.molchanov.fishingforecast.DeleteForecastSetting
import dmitry.molchanov.fishingforecast.MainViewModel
import dmitry.molchanov.fishingforecast.SaveForecastSettingMark

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