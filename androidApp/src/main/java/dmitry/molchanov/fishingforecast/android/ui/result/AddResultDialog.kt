package dmitry.molchanov.fishingforecast.android.ui.result

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmitry.molchanov.fishingforecast.android.ui.DropDown
import java.util.*

@Composable
fun AddResultDialog(vm: ResultViewModel) {
    val resultState = vm.stateFlow.collectAsState()
    val selectedProfile = resultState.value.selectedProfile
    val mapPointsBySelectedProfile = resultState.value.mapPoints
        .filter { it.profileName == selectedProfile.name }
    val profileNames = remember { resultState.value.profiles.map { it.name } }
    val dateLabels = remember {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        resultState.value.dates.map(simpleDateFormat::format)
    }
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
                    label = { Text("Оставьте комментарий") }
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
                        .clickable { vm.onAction(CreateResult()) }
                )
            }
        }
    )
}