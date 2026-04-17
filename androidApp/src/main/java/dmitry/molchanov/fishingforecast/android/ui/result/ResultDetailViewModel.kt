package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.model.WeatherDate
import dmitry.molchanov.domain.usecase.GetWeatherDataByResultUseCase
import dmitry.molchanov.domain.utils.ONE_DAY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultDetailViewModel(
    private val result: Result,
    getWeatherDataByResultUseCase: GetWeatherDataByResultUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ResultDetailState())
    val stateFlow = _stateFlow.asStateFlow()

    private var allWeatherData: List<WeatherData> = emptyList()
    private var resultDate: WeatherDate? = null

    init {
        viewModelScope.launch {
            val weatherData = getWeatherDataByResultUseCase.execute(result)
            allWeatherData = weatherData
            // Определяем дату результата (максимальная дата из погоды как ориентир)
            resultDate = weatherData.maxByOrNull { it.date.roundedValue }?.date
            _stateFlow.update { it.copy(weatherData = weatherData) }
        }
    }

    fun onAction(action: ResultDetailAction) {
        when (action) {
            is OnDateSelected -> {
                val selectedDate = action.date
                resultDate = selectedDate
                // Генерируем 6 дней: 3 до + день рыбалки + 2 после
                val roundedValue = selectedDate.roundedValue
                val dates = (0 until 6).map { offset ->
                    roundedValue - 3 * ONE_DAY + offset * ONE_DAY
                }

                val weatherMap = allWeatherData.associateBy { it.date.roundedValue }
                val filledWeatherData = dates.mapIndexed { index, dateTs ->
                    val wd = weatherMap[dateTs]
                    if (wd != null) {
                        wd
                    } else {
                        // Пустая ячейка
                        WeatherData(
                            id = -1L - index,
                            date = dateTs.toWeatherDate(),
                            mapPoint = result.mapPoint,
                            pressure = null,
                            temperature = null,
                            wind = null,
                            humidity = null,
                            moonCode = null
                        )
                    }
                }

                _stateFlow.update {
                    it.copy(
                        selectedDate = selectedDate,
                        weatherData = filledWeatherData
                    )
                }
            }
        }
    }
}

private fun Long.toWeatherDate(): WeatherDate {
    val cal = java.util.Calendar.getInstance()
    cal.timeInMillis = this
    val dayPart = dmitry.molchanov.domain.utils.DayPart.MIDDAY
    return WeatherDate(
        roundedValue = this,
        year = cal.get(java.util.Calendar.YEAR),
        month = cal.get(java.util.Calendar.MONTH) + 1,
        day = cal.get(java.util.Calendar.DAY_OF_MONTH),
        dayPart = dayPart
    )
}

data class ResultDetailState(
    val selectedDate: WeatherDate? = null,
    val weatherData: List<WeatherData> = emptyList()
)

sealed class ResultDetailAction
class OnDateSelected(val date: WeatherDate) : ResultDetailAction()
