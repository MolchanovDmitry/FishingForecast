package dmitry.molchanov.fishingforecast.android.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun ProfileColumn(
    radioOptions: List<String> = listOf("Profile1", "Profile2", "Profile3"),
    defaultOption: String = "Profile2",
    onOptionSelected: (String) -> Unit = {},
    deleteOption: (String) -> Unit = {}
) {
    if (radioOptions.isEmpty()) return
    val selectedOption = remember {
        var defaultIndex = 0
        radioOptions.forEachIndexed { index, option ->
            if (option == defaultOption) defaultIndex = index
        }
        mutableStateOf(radioOptions[defaultIndex])
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(radioOptions) { item ->
            Row(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .padding(end = 4.dp)
                    .clickable {
                        selectedOption.value = item
                        onOptionSelected(item)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (item == selectedOption.value),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary,
                        unselectedColor = MaterialTheme.colors.primary
                    ),
                    onClick = null
                )
                Text(
                    text = item,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1F).padding(8.dp)
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable { deleteOption(item)  }
                )
            }
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}