package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResultDetailViewModel : ViewModel() {

    private val _stateFlow = MutableStateFlow(ResultDetailState())
    val stateFlow = _stateFlow.asStateFlow()

    fun onAction(action: ResultDetailAction) {
        when (action) {
            is OnDateSelected -> _stateFlow.update { it.copy(selectedDate = action.date) }
        }
    }
}

data class ResultDetailState(
    val selectedDate: Long? = null,
)

sealed class ResultDetailAction
class OnDateSelected(val date: Long) : ResultDetailAction()