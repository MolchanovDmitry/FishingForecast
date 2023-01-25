package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmitry.molchanov.domain.model.Result
import dmitry.molchanov.domain.model.WeatherData
import dmitry.molchanov.domain.usecase.GetWeatherDataByResultUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultDetailViewModel(
    result: Result,
    getWeatherDataByResultUseCase: GetWeatherDataByResultUseCase,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ResultDetailState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val weatherData = getWeatherDataByResultUseCase.execute(result)
            _stateFlow.update { it.copy(weatherData = weatherData) }
        }
    }

    fun onAction(action: ResultDetailAction) {
        when (action) {
            is OnDateSelected -> _stateFlow.update { it.copy(selectedDate = action.date) }
        }
    }

}

data class ResultDetailState(
    val selectedDate: Long? = null,
    val weatherData: List<WeatherData> = emptyList()
)

sealed class ResultDetailAction
class OnDateSelected(val date: Long) : ResultDetailAction()