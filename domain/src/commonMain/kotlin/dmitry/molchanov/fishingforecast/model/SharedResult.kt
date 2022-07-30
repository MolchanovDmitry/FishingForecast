package dmitry.molchanov.fishingforecast.model

import kotlinx.serialization.Serializable

@Serializable
class SharedResult(
    val result: Result,
    val weatherData: List<WeatherData>,
)