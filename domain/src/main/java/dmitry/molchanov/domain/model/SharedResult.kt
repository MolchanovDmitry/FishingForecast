package dmitry.molchanov.fishingforecast.model

import dmitry.molchanov.domain.model.WeatherData
import kotlinx.serialization.Serializable

@Serializable
class SharedResult(
    val result: Result,
    val weatherData: List<WeatherData>,
)
