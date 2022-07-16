package dmitry.molchanov.fishingforecast.android.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcher
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    getProfilesUseCase: GetProfilesUseCase,
    commonProfileFetcher: CommonProfileFetcher,
    getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val saveProfileUseCase: Lazy<SaveProfileUseCase>,
    private val deleteProfileUseCase: Lazy<DeleteProfileUseCase>,
    private val selectProfileUseCase: Lazy<SelectProfileUseCase>,

    ) : ViewModel() {

    private val stateFlow = MutableStateFlow(ProfileViewState())
    val state = stateFlow.asStateFlow()

    init {
        getProfilesUseCase.executeFlow().onEach { profiles ->
            stateFlow.update { it.copy(profiles = profiles + commonProfileFetcher.get()) }
        }.launchIn(viewModelScope)
        getCurrentProfileUseCase.execute()
            .onEach { profile ->
                stateFlow.update { it.copy(currentProfile = profile) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is CreateProfile -> createProfile(action.name)
            is DeleteProfile -> deleteProfile(action.name)
            is SelectProfile -> selectProfile(action.name)
        }
    }

    private fun selectProfile(name: Profile) {
        viewModelScope.launch {
            selectProfileUseCase.value.execute(name)
        }
    }

    private fun deleteProfile(name: Profile) {
        viewModelScope.launch {
            deleteProfileUseCase.value.execute(name)
        }
    }

    private fun createProfile(name: Profile) {
        viewModelScope.launch {
            saveProfileUseCase.value.execute(name)
        }
    }
}

data class ProfileViewState(
    val currentProfile: Profile = Profile("", isCommon = true),
    val profiles: List<Profile> = emptyList(),
)

sealed class ProfileAction

class CreateProfile(val name: Profile) : ProfileAction()
class SelectProfile(val name: Profile) : ProfileAction()
class DeleteProfile(val name: Profile) : ProfileAction()