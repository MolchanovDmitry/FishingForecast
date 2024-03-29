package dmitry.molchanov.fishingforecast.android.ui.result

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmitry.molchanov.fishingforecast.android.ui.DropDown
import dmitry.molchanov.fishingforecast.android.ui.showToast
import java.util.*

@Composable
fun AddResultDialog(vm: ResultViewModel) {
    val resultState = vm.stateFlow.collectAsState()
    val selectedProfile = resultState.value.selectedProfile
    val mapPointsBySelectedProfile = resultState.value.mapPoints
        .filter { mapPoint -> mapPoint.profile == selectedProfile }
    val profileNames = remember { resultState.value.profiles.map { it.name } }
    val dateLabels = remember {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        resultState.value.dates.map(simpleDateFormat::format)
    }
    val context = LocalContext.current
    var commentText by remember { mutableStateOf("") }
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { vm.onAction(CloseAddResultDialog()) },
        text = {
            Column {
                Text("Введите данные для сохранения")
                DropDown(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Выберите профиль",
                    suggestions = profileNames,
                    defaultSelectedIndex = profileNames.indexOf(selectedProfile.name),
                    onSelectIndex = { profileIndex ->
                        resultState.value.profiles.getOrNull(profileIndex)
                            ?.let(::ProfileSelected)
                            ?.let(vm::onAction)
                    }
                )
                DropDown(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Выберите точку",
                    suggestions = mapPointsBySelectedProfile.map { it.name },
                    defaultSelectedIndex = resultState.value.selectedMapPoint
                        ?.let(mapPointsBySelectedProfile::indexOf)
                        ?: 0,
                    onSelectIndex = { mapPointIndex ->
                        mapPointsBySelectedProfile.getOrNull(mapPointIndex)
                            ?.let(::MapPointSelected)
                            ?.let(vm::onAction)
                    }
                )
                DropDown(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Выберите дату",
                    suggestions = dateLabels,
                    defaultSelectedIndex = resultState.value.dates.indexOf(resultState.value.selectedDate),
                    onSelectIndex = { dateIndex ->
                        resultState.value.dates.getOrNull(dateIndex)
                            ?.let(::DateSelected)
                            ?.let(vm::onAction)
                    }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("Наименование результата") }
                )
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
                        .clickable { vm.onAction(CloseAddResultDialog()) }
                )
                Text(
                    "Сохранить",
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (commentText.isNotEmpty()) {
                                vm.onAction(CreateResult(resultName = commentText))
                                vm.onAction(CloseAddResultDialog())
                            } else {
                                context.showToast("Не заполнено наименование результата")
                            }
                        }
                )
            }
        }
    )
}