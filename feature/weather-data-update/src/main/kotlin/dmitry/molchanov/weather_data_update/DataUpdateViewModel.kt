package dmitry.molchanov.weather_data_update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.repository.WeatherDataRepository
import dmitry.molchanov.domain.usecase.SaveWeatherDataUseCase
import dmitry.molchanov.domain.utils.getDayCount
import dmitry.molchanov.domain.utils.getMonthCount
import dmitry.molchanov.weather_data_update.UpdateStatus.ERROR
import dmitry.molchanov.weather_data_update.UpdateStatus.NO_UPDATE_REQUIRED
import dmitry.molchanov.weather_data_update.UpdateStatus.UPDATE_IN_PROGRESS
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DataUpdateViewModel(
    weatherDataRepository: WeatherDataRepository,
    private val saveWeatherDataUseCase: SaveWeatherDataUseCase
) : ViewModel() {

    private val _weatherDataStateFlow = MutableStateFlow(WeatherDataState())
    val weatherDataStateFlow = _weatherDataStateFlow.asStateFlow()

    private val _messageFlow = MutableSharedFlow<DataUpdateEvent>(extraBufferCapacity = 1)
    val messageFlow = _messageFlow.asSharedFlow()

    init {
        weatherDataRepository
            .fetchAllWeatherData()
            .onEach(::onWeatherDataFetched)
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun onWeatherDataFetched(weatherData: List<WeatherData>) {
        weatherData
            .groupBy { it.mapPoint }
            .map { pointToWeatherMap ->
                val mapPoint = pointToWeatherMap.key
                val lastDate = pointToWeatherMap.value.maxOf { it.date }
                UiWeatherData(
                    mapPoint = mapPoint,
                    lastUpdateTime = lastDate,
                    status = if (shouldUpdate(date = lastDate)) UPDATE_IN_PROGRESS else NO_UPDATE_REQUIRED
                )
            }.let { uiWeatherDataList ->
                _weatherDataStateFlow.update { it.copy(list = uiWeatherDataList) }
            }
        update()
    }

    fun update() {
        viewModelScope.launch {
            _weatherDataStateFlow.value.list
                .filter { uiWeatherData -> shouldUpdate(uiWeatherData.lastUpdateTime) }
                .forEach { uiWeatherData ->
                    saveWeatherDataUseCase.execute(uiWeatherData.mapPoint)
                        .onFailure { error ->
                            _weatherDataStateFlow.value =
                                weatherDataStateFlow.value.list
                                    .toMutableList()
                                    .apply {
                                        remove(uiWeatherData)
                                        add(uiWeatherData.copy(status = ERROR))
                                    }.let(::WeatherDataState)

                            when (error) {
                                is UnknownHostException -> NetworkError()
                                else -> UnknownError(message = error.message)
                            }.let(_messageFlow::tryEmit)
                        }
                }
        }
    }

    private fun shouldUpdate(date: Long): Boolean {
        return System.currentTimeMillis().getDayCount() != date.getDayCount() ||
                System.currentTimeMillis().getMonthCount() != date.getMonthCount()
    }
}

data class WeatherDataState(
    val list: List<UiWeatherData> = emptyList()
)

data class UiWeatherData(
    val mapPoint: MapPoint,
    val lastUpdateTime: Long,
    val status: UpdateStatus
)

enum class UpdateStatus {
    NO_UPDATE_REQUIRED,
    UPDATE_IN_PROGRESS,
    UPDATED,
    ERROR
}

sealed class DataUpdateEvent
class UnknownError(val message: String?) : DataUpdateEvent()
class NetworkError : DataUpdateEvent()