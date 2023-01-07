package dmitry.molchanov.fishingforecast.android.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.usecase.GetCurrentProfileUseCase
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcherImpl
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.model.SimpleProfile
import dmitry.molchanov.fishingforecast.usecase.DeleteProfileUseCase
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveProfileUseCase
import dmitry.molchanov.fishingforecast.usecase.SelectProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    getProfilesNamesUseCase: GetProfilesUseCase,
    commonProfileFetcher: CommonProfileFetcherImpl,
    getCurrentProfileNameUseCase: GetCurrentProfileUseCase,
    private val saveProfileUseCase: Lazy<SaveProfileUseCase>,
    private val deleteProfileUseCase: Lazy<DeleteProfileUseCase>,
    private val selectProfileUseCase: Lazy<SelectProfileUseCase>,

    ) : ViewModel() {

    private val stateFlow = MutableStateFlow(ProfileViewState(currentProfile = commonProfileFetcher.instance))
    val state = stateFlow.asStateFlow()

    init {
        getProfilesNamesUseCase.executeFlow().onEach { profiles->
            stateFlow.update {
                it.copy(profiles = profiles)
            }
        }.launchIn(viewModelScope)
        getCurrentProfileNameUseCase.execute().onEach { profile ->
            stateFlow.update { it.copy(currentProfile = profile) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is CreateProfile -> createProfile(action.profile)
            is DeleteProfile -> deleteProfile(action.profile)
            is SelectProfile -> selectProfile(action.profile)
        }
    }

    private fun selectProfile(profile: Profile) {
        viewModelScope.launch {
            selectProfileUseCase.value.execute(profile)
        }
    }

    private fun deleteProfile(name: Profile) {
        viewModelScope.launch {
            deleteProfileUseCase.value.execute(name)
        }
    }

    private fun createProfile(profile: Profile) {
        viewModelScope.launch {
            (profile as? SimpleProfile)?.let { simpleProfile ->
                saveProfileUseCase.value.execute(simpleProfile)
            }
        }
    }
}

data class ProfileViewState(
    val currentProfile: Profile,
    val profiles: List<Profile> = emptyList(),
)

sealed class ProfileAction

class CreateProfile(val profile: Profile) : ProfileAction()
class SelectProfile(val profile: Profile) : ProfileAction()
class DeleteProfile(val profile: Profile) : ProfileAction()