package dmitry.molchanov.fishingforecast.android.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmitry.molchanov.domain.model.Profile
import dmitry.molchanov.domain.model.SimpleProfile
import dmitry.molchanov.fishingforecast.android.ui.common.BaseDialog

@Composable
fun ProfileColumn(
    profiles: List<Profile>,
    defaultOption: Profile,
    onOptionSelected: (Profile) -> Unit = {},
    deleteOption: (Profile) -> Unit = {}
) {
    if (profiles.isEmpty()) return
    val selectedOption = remember {
        var defaultIndex = 0
        profiles.forEachIndexed { index, option ->
            if (option == defaultOption) defaultIndex = index
        }
        mutableStateOf(profiles[defaultIndex])
    }
    var profileToDelete by remember { mutableStateOf<Profile?>(null) }
    Box(modifier = Modifier.fillMaxSize()) {
        if (profileToDelete != null) {
            BaseDialog(
                message = "Удалить профиль ${profileToDelete?.name} ?",
                onPositive = {
                    profileToDelete?.let { deleteOption(it) }
                    profileToDelete = null
                },
                onNegative = { profileToDelete = null }
            )
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(items = profiles) { profile ->
                Row(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .padding(end = 4.dp)
                        .clickable {
                            selectedOption.value = profile
                            onOptionSelected(profile)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (profile == selectedOption.value),
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary,
                            unselectedColor = MaterialTheme.colors.primary
                        ),
                        onClick = null
                    )
                    Text(
                        text = profile.name,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1F)
                            .padding(8.dp)
                    )
                    if (profile is SimpleProfile) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.clickable { profileToDelete = profile }
                        )
                    }
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
