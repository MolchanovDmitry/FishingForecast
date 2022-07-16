package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val getProfilesUseCase: GetProfilesUseCase,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ResultScreenState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        updateProfiles()
    }

    fun onAction(action: ResultAction) = when (action) {
        is AddResultClickAction -> showAddDialog()
        is CloseAddResultDialog -> closeAddResultDialog()
        is ProfileSelected -> fetchMapPoints()
    }

    private fun fetchMapPoints() {
        //TODO("Not yet implemented")
    }

    private fun updateProfiles() {
        viewModelScope.launch {
            val profiles = getProfilesUseCase.execute()
            _stateFlow.update { it.copy(profiles = profiles) }
        }
    }

    private fun closeAddResultDialog() {
        _stateFlow.update { it.copy(shouldShowDialog = false) }
    }

    private fun showAddDialog() {
        _stateFlow.update { it.copy(shouldShowDialog = true) }
    }

}

data class ResultScreenState(
    val profiles: List<Profile> = emptyList(),
    val shouldShowDialog: Boolean = false
)

sealed class ResultAction
class AddResultClickAction : ResultAction()
class CloseAddResultDialog : ResultAction()

class ProfileSelected(profile: Profile) : ResultAction()