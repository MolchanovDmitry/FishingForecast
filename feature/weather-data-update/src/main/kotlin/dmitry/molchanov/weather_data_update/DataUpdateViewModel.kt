package dmitry.molchanov.weather_data_update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.WeatherDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DataUpdateViewModel(
    weatherDataRepository: WeatherDataRepository,
) : ViewModel() {

    private val _weatherDataStateFlow = MutableStateFlow(WeatherDataState())
    val weatherDataStateFlow = _weatherDataStateFlow.asStateFlow()

    init {
        weatherDataRepository
            .fetchAllWeatherData()
            .onEach(::onWeatherDataFetched)
            .launchIn(viewModelScope)
        updateWeatherData()
    }

    fun updateWeatherData() {

    }

    private fun onWeatherDataFetched(weatherData: List<WeatherData>) {
        weatherData
            .groupBy { it.mapPoint }
            .map { pointToWeatherMap ->
                val mapPoint = pointToWeatherMap.key
                val lastDate = pointToWeatherMap.value.maxOf { it.date }
                UiWeatherData(
                    pointId = mapPoint.id,
                    pointName = mapPoint.name,
                    lastUpdateTime = lastDate
                )
            }.let { uiWeatherDataList ->
                _weatherDataStateFlow.update { it.copy(list = uiWeatherDataList) }
            }
    }

}

data class WeatherDataState(
    val list: List<UiWeatherData> = emptyList()
)

data class UiWeatherData(
    val pointId: Long,
    val pointName: String,
    val lastUpdateTime: Long
)