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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dmitry.molchanov.fishingforecast.android.*
import dmitry.molchanov.fishingforecast.model.Profile

@Composable
fun ProfileScreen(vm: MainViewModel) {
    val state = vm.state.collectAsState()
    val profiles = state.value.profiles.map {
        if (it.name.isEmpty()) {
            stringResource(R.string.common_profile)
        } else {
            it.name
        }

    }
    val openCreateDialog = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileColumn(
            radioOptions = profiles,
            defaultOption = state.value.currentProfile.name,
            deleteOption = { profileName ->
                vm.onEvent(DeleteProfile(Profile(profileName)))
            },
            onOptionSelected = { profileName ->
                vm.onEvent(SelectProfile(Profile(profileName)))
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
        CreateProfileDialog(openCreateDialog, profiles) { profileName ->
            vm.onEvent(CreateProfile(Profile(profileName)))
        }
    }
}