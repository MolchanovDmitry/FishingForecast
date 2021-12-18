package dmitry.molchanov.fishingforecast.android.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmitry.molchanov.fishingforecast.android.CreateProfile
import dmitry.molchanov.fishingforecast.android.DeleteProfile
import dmitry.molchanov.fishingforecast.android.MainViewModel
import dmitry.molchanov.fishingforecast.android.SelectProfile

@Composable
fun ProfileScreen(vm: MainViewModel) {
    val profiles by remember { mutableStateOf(listOf("Profile 1", "Profile 2")) }
    val openCreateDialog = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileColumn(
            radioOptions = profiles,
            defaultOption = "Profile 1",
            deleteOption = { profileName ->
                vm.onEvent(DeleteProfile(profileName))
            },
            onOptionSelected = { profileName ->
                vm.onEvent(SelectProfile(profileName))
            }
        )
        Icon(
            imageVector = Icons.Filled.PersonAddAlt,
            contentDescription = "Add",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(90.dp)
                .padding(16.dp)
                .clickable {
                    openCreateDialog.value = true
                }
        )
        CreateProfileDialog(openCreateDialog) { profileName ->
            vm.onEvent(CreateProfile(profileName))
        }
    }
}