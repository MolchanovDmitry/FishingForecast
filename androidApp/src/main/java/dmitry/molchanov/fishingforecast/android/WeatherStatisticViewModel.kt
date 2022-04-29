package dmitry.molchanov.fishingforecast.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.fishingforecast.model.MapPoint
import dmitry.molchanov.fishingforecast.model.WeatherData
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import kotlinx.coroutines.flow.*

data class WeatherStatisticViewState(
    val weatherData: List<WeatherData> = emptyList()
)

class WeatherStatisticViewModel(
    private val mapPoint: MapPoint,
    private val weatherDataRepository: WeatherDataRepository
) : ViewModel() {

    private val state = MutableStateFlow(WeatherStatisticViewState())
    var stateFlow = state.asStateFlow()

    init {



        weatherDataRepository.fetchWeatherData()
            .onEach { weatherData ->
                state.update { it.copy(weatherData = weatherData) }
            }.launchIn(viewModelScope)
    }

}

