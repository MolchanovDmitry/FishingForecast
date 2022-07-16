package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmitry.molchanov.fishingforecast.android.ui.DropDown

@Composable
fun AddResultDialog(
    vm: ResultViewModel,
    dismiss: () -> Unit
) {
    val resultState = vm.stateFlow.collectAsState()
    val selectedProfile = resultState.value.selectedProfile
    val mapPointsBySelectedProfile = resultState.value.mapPoints
        .filter { it.profileName == selectedProfile.name || (selectedProfile.isCommon && it.profileName == null) }
    val profileNames = remember { resultState.value.profiles.map { it.name } }
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { dismiss() },
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
                    defaultSelectedIndex = 0,
                    onSelectIndex = { mapPointIndex ->
                        mapPointsBySelectedProfile.getOrNull(mapPointIndex)
                            ?.let(::MapPointSelected)
                            ?.let(vm::onAction)
                    }
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
                        .clickable {
                            dismiss()
                        }
                )
                Text(
                    "Сохранить",
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            dismiss()
                        }
                )
            }
        }
    )
}