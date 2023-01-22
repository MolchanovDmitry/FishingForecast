package dmitry.molchanov.fishingforecast.android.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun BaseDialog(
    message: String = "Сообщение",
    openDialog: MutableState<Boolean> = mutableStateOf(true),
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {}
) {
    if (openDialog.value) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {
                onNegative()
                openDialog.value = false
            },
            text = {
                Text(message)
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text("Отменить",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onNegative()
                                openDialog.value = false
                            })
                    Text(
                        "Сохранить",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onPositive()
                                openDialog.value = false
                            })
                }
            }
        )
    }
}