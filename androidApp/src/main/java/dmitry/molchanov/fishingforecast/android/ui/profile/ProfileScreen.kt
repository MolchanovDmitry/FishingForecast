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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel

@Composable
fun ProfileScreen() {
    val vm by viewModel<ProfileViewModel>()
    val state = vm.state.collectAsState()
    val profiles = state.value.profiles

    val openCreateDialog = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileColumn(
            profiles = profiles,
            defaultOption = state.value.currentProfile,
            deleteOption = { profile ->
                vm.onAction(DeleteProfile(profile))
            },
            onOptionSelected = { profile ->
                vm.onAction(SelectProfile(profile))
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
        CreateProfileDialog(openCreateDialog, profiles) { profile ->
            vm.onAction(CreateProfile(profile))
        }
    }
}