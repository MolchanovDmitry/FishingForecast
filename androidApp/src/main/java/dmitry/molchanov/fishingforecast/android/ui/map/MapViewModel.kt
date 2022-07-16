package dmitry.molchanov.fishingforecast.android.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcher
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.GetCurrentProfileUseCase
import dmitry.molchanov.fishingforecast.usecase.GetMapPointsUseCase
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveMapPointUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(
    getProfilesUseCase: GetProfilesUseCase,
    getMapPointsUseCase: GetMapPointsUseCase,
    getCurrentProfileUseCase: GetCurrentProfileUseCase,
    commonProfileFetcher: CommonProfileFetcher,
    private val saveMapPointUseCase: Lazy<SaveMapPointUseCase>,
) : ViewModel() {

    private val stateFlow = MutableStateFlow(MapViewState())
    val state = stateFlow.asStateFlow()

    private var allMapPoints: List<MapPoint> = emptyList()

    init {
        getCurrentProfileUseCase.execute()
            .onEach { profile ->
                stateFlow.update {
                    it.copy(
                        currentProfile = profile,
                        mapPoints = if (profile.isCommon) {
                            allMapPoints
                        } else {
                            allMapPoints.filter { it.profileName == profile.name }
                        }
                    )
                }
            }
            .launchIn(viewModelScope)

        getMapPointsUseCase.executeFlow()
            .onEach { mapPoints ->
                this.allMapPoints = mapPoints
                stateFlow.update {
                    it.copy(mapPoints =
                    if (state.value.currentProfile.isCommon) {
                        mapPoints
                    } else {
                        mapPoints.filter { it.profileName == state.value.currentProfile.name }
                    })
                }
            }
            .launchIn(viewModelScope)

        getProfilesUseCase.executeFlow().onEach { profiles ->
            stateFlow.update { it.copy(profiles = profiles + commonProfileFetcher.get()) }
        }.launchIn(viewModelScope)

        getMapPointsUseCase.executeFlow().onEach { mapPoints ->
            //this.allMapPoints = mapPoints
            stateFlow.update {
                it.copy(mapPoints = if (state.value.currentProfile.isCommon) {
                    mapPoints
                } else {
                    mapPoints.filter { it.profileName == state.value.currentProfile.name }
                })
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: MapAction) {
        when (action) {
            is SavePoint -> saveMapPoint(action)
        }
    }

    private fun saveMapPoint(action: SavePoint) {
        viewModelScope.launch {
            saveMapPointUseCase.value.execute(
                pointName = action.title,
                profile = action.profile,
                latitude = action.latitude,
                longitude = action.longitude,
            )
        }
    }
}

data class MapViewState(
    val currentProfile: Profile = Profile("", isCommon = true),
    val mapPoints: List<MapPoint> = emptyList(),
    val profiles: List<Profile> = emptyList(),
)

sealed class MapAction
data class SavePoint(
    val title: String, val profile: Profile, val latitude: Double, val longitude: Double
) : MapAction()