package dmitry.molchanov.weather_data_update

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.util.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun DataUpdateScreen() {
    val vm = koinViewModel<DataUpdateViewModel>()
    val state = vm.weatherDataStateFlow.collectAsState()
    val formatter = remember {
         SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
    }

    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(state.value.list){ item ->
            Column(modifier =  Modifier.fillMaxWidth()) {
                Text(text = item.pointName)
                Text(text = "Дата последнего обновления: ${formatter.format(item.lastUpdateTime)}")
            }
        }
    }


}