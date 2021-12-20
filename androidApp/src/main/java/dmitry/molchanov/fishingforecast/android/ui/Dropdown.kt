package dmitry.molchanov.fishingforecast.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize

@Preview
@Composable
fun DropDown(
    modifier: Modifier = Modifier,
    label: String = "",
    suggestions: List<String> = listOf("red", "green", "blue"),
    defaultSelectedIndex: Int = 0,
    onSelectIndex: (Int) -> Unit = {}
) {
    val expanded = remember { mutableStateOf(false) }
    val textFieldSize = remember { mutableStateOf(Size.Zero) }
    var selectIndex by remember { mutableStateOf(0)}
    val icon = if (expanded.value) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    Column(modifier = modifier) {
        OutlinedTextField(
            value = suggestions[selectIndex],
            label = { Text(label) },
            onValueChange = { },
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textFieldSize.value = coordinates.size.toSize()
            },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded.value = !expanded.value })
            }
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.value.width.toDp() })
        ) {
            suggestions.forEachIndexed { index, label ->
                DropdownMenuItem(onClick = {
                    println("1488 index = ${index} label = ${label}")
                    onSelectIndex(index)
                    selectIndex = index
                    expanded.value = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}