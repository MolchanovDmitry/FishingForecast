package dmitry.molchanov.fishingforecast.android.ui.map

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
import dmitry.molchanov.fishingforecast.model.Profile

@Composable
fun CreateMapPointDialog(
    openDialog: MutableState<Boolean>,
    profiles: List<Profile>,
    createMapPoint: (String, Profile) -> Unit
) {
    var profileEdit by remember { mutableStateOf("") }
    var profileIndex by remember { mutableStateOf(0) }

    if (openDialog.value) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {
                openDialog.value = false
            },
            text = {
                Column {
                    Text(
                        text = "Создание точки наблюдения",
                        color = MaterialTheme.colors.primary,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .padding(bottom = 8.dp)
                    )
                    TextField(
                        value = profileEdit,
                        onValueChange = { profileEdit = it },
                        label = { Text("Введите наименование точки.") }
                    )
                    DropDown(
                        modifier = Modifier.fillMaxWidth(),
                        label = "",
                        suggestions = profiles.map { it.name },
                        defaultSelectedIndex = 0,
                        onSelectIndex = { index ->
                            profileIndex = index
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
                            .clickable { openDialog.value = false }
                    )
                    Text(
                        "Сохранить",
                        color = MaterialTheme.colors.primary,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                openDialog.value = false
                                createMapPoint(profileEdit, profiles[profileIndex])
                            }
                    )
                }
            }
        )
    } else {
        profileEdit = ""
    }
}