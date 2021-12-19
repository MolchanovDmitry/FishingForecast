package dmitry.molchanov.fishingforecast.android

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.data.DriverFactory
import dmitry.molchanov.data.MapPointRepositoryImpl
import dmitry.molchanov.data.ProfileRepositoryImpl
import dmitry.molchanov.db.AppDatabase
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.Profile
import dmitry.molchanov.fishingforecast.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val saveMapPointUseCase: SaveMapPointUseCase,
    private val getMapPointsUseCase: GetMapPointsUseCase,
    private val getProfilesUseCase: GetProfilesUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState(emptyList(), emptyList()))
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    init {

        getProfilesUseCase
            .execute()
            .map {
                // TODO слить 2 flow в будущем.
                it to getMapPointsUseCase.execute()
            }
            .flowOn(Dispatchers.IO)
            .onEach {
                _state.value = MainViewState(it.second, it.first)
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is SavePoint -> saveMapPoint(event)
            is CreateProfile -> createProfile(event.name)
            is DeleteProfile -> deleteProfile(event.name)
            is SelectProfile -> selectProfile(event.name)
        }
    }

    private fun saveMapPoint(event: SavePoint) {
        viewModelScope.launch {
            saveMapPointUseCase.execute(
                Profile(""),
                MapPoint("", latitude = event.latitude, longitude = event.longitude)
            )
        }
    }

    private fun selectProfile(name: String) {

    }

    private fun deleteProfile(name: String) {
        viewModelScope.launch {
            deleteProfileUseCase.execute(Profile(name))
        }

    }

    private fun createProfile(name: String) {
        viewModelScope.launch {
            saveProfileUseCase.execute(Profile(name))
        }
    }
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val driver = DriverFactory(context.applicationContext).createDriver()
        val database = AppDatabase(driver)
        val mapPointRepository = MapPointRepositoryImpl()
        val profileRepository = ProfileRepositoryImpl(database)
        val saveMapPointUseCase = SaveMapPointUseCase(mapPointRepository)
        val getMapPointsUseCase = GetMapPointsUseCase(mapPointRepository)
        val getProfileUseCase = GetProfilesUseCase(profileRepository)
        val createProfileUseCase = SaveProfileUseCase(profileRepository)
        val deleteProfileUseCase = DeleteProfileUseCase(profileRepository)
        return MainViewModel(
            saveMapPointUseCase,
            getMapPointsUseCase,
            getProfileUseCase,
            createProfileUseCase,
            deleteProfileUseCase
        ) as T
    }

}

data class MainViewState(
    val mapPoints: List<MapPoint>,
    val profiles: List<String>
)

sealed class Event

class CreateProfile(val name: String) : Event()
class SelectProfile(val name: String) : Event()
class DeleteProfile(val name: String) : Event()
data class SavePoint(val latitude: Double, val longitude: Double) : Event()