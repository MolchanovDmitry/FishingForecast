package dmitry.molchanov.weather_data_update

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dmitry.molchanov.weather_data_update.UpdateStatus.ERROR
import dmitry.molchanov.weather_data_update.UpdateStatus.NO_UPDATE_REQUIRED
import dmitry.molchanov.weather_data_update.UpdateStatus.UPDATED
import dmitry.molchanov.weather_data_update.UpdateStatus.UPDATE_IN_PROGRESS
import java.util.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

private const val COLUMN_1_WEIGHT = .3f
private const val COLUMN_2_WEIGHT = .4f
private const val COLUMN_3_WEIGHT = .3f

@Composable
fun DataUpdateScreen() {
    val context = LocalContext.current
    val vm = koinViewModel<DataUpdateViewModel>()
    val state = vm.weatherDataStateFlow.collectAsState()
    val formatter = remember {
        SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                Row(Modifier) {
                    TableCell(text = "Точка", weight = COLUMN_1_WEIGHT)
                    TableCell(text = "Дата обновления", weight = COLUMN_2_WEIGHT)
                    TableCell(text = "Статус", weight = COLUMN_3_WEIGHT)
                }
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
            itemsIndexed(state.value.list) { index, item ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    TableCell(text = item.mapPoint.name, weight = COLUMN_1_WEIGHT)
                    TableCell(
                        text = formatter.format(item.lastUpdateTime),
                        weight = COLUMN_2_WEIGHT
                    )
                    StatusCell(status = item.status, weight = COLUMN_3_WEIGHT)
                }
                if (index < state.value.list.lastIndex) {
                    Divider(color = Color.LightGray, thickness = 1.dp)
                }
            }
        }
        Button(
            onClick = { vm.update() }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(BottomCenter)
        ) {
            Text(text = stringResource(id = R.string.update))
        }
    }
    LaunchedEffect(key1 = Unit) {
        vm.messageFlow.onEach { event ->
            when (event) {
                is NetworkError -> context.getString(R.string.error_network)
                is UnknownError -> event.message
            }?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }.launchIn(this)
    }
}

@Composable
private fun RowScope.TableCell(text: String, weight: Float) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .align(CenterVertically)
    )
}

@Composable
private fun RowScope.StatusCell(status: UpdateStatus, weight: Float) {
    Row(
        Modifier
            .weight(weight)
            .padding(top = 8.dp, bottom = 8.dp)
            .align(CenterVertically)
    ) {
        if (status == UPDATE_IN_PROGRESS) {
            CircularProgressIndicator(
                Modifier
                    .size(15.dp)
                    .align(CenterVertically),
            )
        } else {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .align(CenterVertically)
                    .background(if (status == ERROR) Color.Red else Color.Green)
            )
        }
        Text(text = getStringStatus(status), Modifier.padding(start = 4.dp))
    }

}

@Composable
private fun getStringStatus(status: UpdateStatus): String {
    return when (status) {
        NO_UPDATE_REQUIRED -> stringResource(R.string.no_update_required)
        UPDATE_IN_PROGRESS -> stringResource(R.string.update_in_progress)
        UPDATED -> stringResource(R.string.updated)
        ERROR -> stringResource(R.string.update_error)
    }
}