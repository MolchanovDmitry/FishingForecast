package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.data.MapPointRepositoryImpl
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.GetMapPointsUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveMapPointUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val saveMapPointUseCase: SaveMapPointUseCase,
    private val getMapPointsUseCase: GetMapPointsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState(emptyList()))
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = MainViewState(getMapPointsUseCase.execute())
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is SavePoint -> saveMapPoint(event)
        }
    }

    private fun saveMapPoint(event: SavePoint) {
        viewModelScope.launch {
            saveMapPointUseCase.execute(
                Profile(""),
                MapPoint("", latitude = event.latitude, longitude = event.longitude)
            )
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = getMapPointsUseCase.execute()
            _state.value = MainViewState(mapPoints = result + emptyList())
        }
    }
}

class MainViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val mapPointRepository = MapPointRepositoryImpl()
        val saveMapPointUseCase = SaveMapPointUseCase(mapPointRepository)
        val getMapPointsUseCase = GetMapPointsUseCase(mapPointRepository)
        return MainViewModel(saveMapPointUseCase, getMapPointsUseCase) as T
    }

}

data class MainViewState(
    val mapPoints: List<MapPoint>
)

sealed class Event

data class SavePoint(val latitude: Double, val longitude: Double) : Event()