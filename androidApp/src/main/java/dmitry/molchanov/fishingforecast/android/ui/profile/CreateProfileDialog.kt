package dmitry.molchanov.fishingforecast.android.ui.profile

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

@Composable
fun CreateProfileDialog(openDialog: MutableState<Boolean>, profileTyped: (String) -> Unit) {
    var profileEdit by remember { mutableStateOf("") }

    if (openDialog.value) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {
                openDialog.value = false
            },
            text = {
                Column {
                    Text(
                        text = "Создайте новый профиль",
                        color = MaterialTheme.colors.primary,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .padding(bottom = 8.dp)
                    )
                    TextField(
                        value = profileEdit,
                        onValueChange = { profileEdit = it }
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
                                profileTyped(profileEdit)
                            }
                    )
                }
            }
        )
    }
}