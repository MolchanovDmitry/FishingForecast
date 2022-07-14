package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.viewModel

@Composable
fun ResultScreen() {
    val vm by viewModel<ResultViewModel>()
    val state = vm.stateFlow.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {

        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(90.dp)
                .padding(16.dp)
                .clickable {
                    vm.onAction(AddResultClickAction())
                }
        )
    }
    if (state.value.shouldShowDialog) {
        AddResultDialog {
            vm.onAction(CloseAddResultDialog())
        }
    }
}