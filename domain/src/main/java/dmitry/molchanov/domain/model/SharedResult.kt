package dmitry.molchanov.domain.model

import kotlinx.serialization.Serializable

@Serializable
class SharedResult(
    val result: Result,
    val weatherData: List<WeatherData>,
)
