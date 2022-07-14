package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResultViewModel : ViewModel() {

    private val _stateFlow = MutableStateFlow(ResultScreenState())
    val stateFlow = _stateFlow.asStateFlow()

    fun onAction(action: ResultAction) = when (action) {
        is AddResultClickAction -> showAddDialog()
        is CloseAddResultDialog -> closeAddResultDialog()
    }

    private fun closeAddResultDialog() {
        _stateFlow.update { it.copy(shouldShowDialog = false) }
    }

    private fun showAddDialog() {
        _stateFlow.update { it.copy(shouldShowDialog = true) }
    }

}

data class ResultScreenState(
    val shouldShowDialog: Boolean = false
)

sealed class ResultAction
class AddResultClickAction : ResultAction()
class CloseAddResultDialog : ResultAction()