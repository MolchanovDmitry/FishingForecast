package dmitry.molchanov.fishingforecast.android.ui.weather

import androidx.lifecycle.ViewModel

class WeatherDebugViewModel : ViewModel() {

    fun onEvent(event: WeatherDebugEvent) {
        when(event) {
            FetchWeatherDataFromApi -> fetchWeatherDataFromApi()
        }
    }

    private fun fetchWeatherDataFromApi() {
        TODO("Not yet implemented")
    }
}

sealed class WeatherDebugEvent
object FetchWeatherDataFromApi : WeatherDebugEvent()