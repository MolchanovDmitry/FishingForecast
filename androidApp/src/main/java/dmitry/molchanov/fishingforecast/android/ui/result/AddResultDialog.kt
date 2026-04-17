package dmitry.molchanov.fishingforecast.android.ui.result

import android.icu.text.SimpleDateFormat
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
    var descriptionText by remember { mutableStateOf("") }
    var currentRating by remember { mutableStateOf(0) }
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { vm.onAction(CloseAddResultDialog()) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
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
                // Звёзды рейтинга
                Text("Оценка:", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= currentRating) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "$i звёзд",
                            tint = if (i <= currentRating)
                                androidx.compose.ui.graphics.Color(0xFFFFC107)
                            else
                                androidx.compose.ui.graphics.Color.LightGray,
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
                    maxLines = 3
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
                                vm.onAction(CreateResultWithDetails(
                                    resultName = commentText,
                                    rating = currentRating,
                                    description = descriptionText
                                ))
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
