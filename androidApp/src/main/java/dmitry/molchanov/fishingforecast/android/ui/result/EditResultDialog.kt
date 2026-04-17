package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditResultDialog(vm: ResultViewModel) {
    val state = vm.stateFlow.collectAsState()
    val editingResult = state.value.editingResult ?: return
    var nameText by remember { mutableStateOf(editingResult.name) }
    var descriptionText by remember { mutableStateOf(editingResult.description ?: "") }
    var currentRating by remember { mutableStateOf(editingResult.rating ?: 0) }

    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { vm.onAction(CloseEditDialog()) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                Text("Редактировать результат", fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text("Название") }
                )

                // Звёзды рейтинга
                Text("Оценка:", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= currentRating) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "$i звёзд",
                            tint = if (i <= currentRating) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { currentRating = i }
                        )
                    }
                }

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    label = { Text("Описание") },
                    maxLines = 4
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
                        .clickable { vm.onAction(CloseEditDialog()) }
                )
                Text(
                    "Сохранить",
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            vm.onAction(SaveEditedResult(
                                resultId = editingResult.id,
                                newName = nameText,
                                newRating = currentRating,
                                newDescription = descriptionText
                            ))
                        }
                )
            }
        }
    )
}
