package dmitry.molchanov.fishingforecast.android.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.usecase.GetCurrentProfileUseCase
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcherImpl
import dmitry.molchanov.fishingforecast.model.CommonProfile
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.GetMapPointsUseCase
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveMapPointUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    getProfilesUseCase: GetProfilesUseCase,
    getMapPointsUseCase: GetMapPointsUseCase,
    getCurrentProfileUseCase: GetCurrentProfileUseCase,
    commonProfileFetcher: CommonProfileFetcherImpl,
    private val saveMapPointUseCase: Lazy<SaveMapPointUseCase>,
) : ViewModel() {

    private val stateFlow =
        MutableStateFlow(MapViewState(currentProfile = commonProfileFetcher.instance))
    val state = stateFlow.asStateFlow()

    private var allMapPoints: List<MapPoint> = emptyList()

    init {
        // TODO запрашивать по профилю
        getCurrentProfileUseCase.execute()
            .onEach { profile ->
                stateFlow.update {
                    it.copy(
                        currentProfile = profile,
                        mapPoints = if (profile is CommonProfile) {
                            allMapPoints
                        } else {
                            allMapPoints.filter { it.profile == profile }
                        }
                    )
                }
            }
            .launchIn(viewModelScope)

        // TODO запрашивать по профилю
        getMapPointsUseCase.executeFlow()
            .onEach { mapPoints ->
                this.allMapPoints = mapPoints
                stateFlow.update {
                    it.copy(
                        mapPoints = if (state.value.currentProfile is CommonProfile) {
                            mapPoints
                        } else {
                            mapPoints.filter { it.profile == state.value.currentProfile }
                        }
                    )
                }
            }
            .launchIn(viewModelScope)

        // TODO запрашивать по профилю
        getProfilesUseCase.executeFlow().onEach { profiles ->
            stateFlow.update { it.copy(profiles = profiles) }
        }.launchIn(viewModelScope)

        // TODO запрашивать по профилю
        getMapPointsUseCase.executeFlow().onEach { mapPoints ->
            // this.allMapPoints = mapPoints
            stateFlow.update {
                it.copy(
                    mapPoints = if (state.value.currentProfile is CommonProfile) {
                        mapPoints
                    } else {
                        mapPoints.filter { it.profile == state.value.currentProfile }
                    }
                )
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
    val currentProfile: Profile,
    val mapPoints: List<MapPoint> = emptyList(),
    val profiles: List<Profile> = emptyList(),
)

sealed class MapAction
data class SavePoint(
    val title: String,
    val profile: Profile,
    val latitude: Double,
    val longitude: Double
) : MapAction()
