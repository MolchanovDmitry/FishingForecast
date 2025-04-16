package dmitry.molchanov.weather_remote

import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Pressure
import dmitry.molchanov.domain.model.RawWeatherData
import dmitry.molchanov.domain.model.Temperature
import dmitry.molchanov.domain.model.Wind
import dmitry.molchanov.domain.model.WindDir
import dmitry.molchanov.domain.repository.YandexWeatherRepository
import dmitry.molchanov.domain.utils.ONE_SEC
import dmitry.molchanov.http.NetworkClient
import dmitry.molchanov.weather_remote.entity.WeatherResponseRoot

class YandexWeatherNetworkDataSourceImpl(
    private val apiKey: String,
) : YandexWeatherRepository {

    override suspend fun getYandexWeatherDate(mapPoint: MapPoint): Result<List<RawWeatherData>> {
        val result = NetworkClient.loadData<WeatherResponseRoot>(
            headers = mapOf("X-Yandex-API-Key" to apiKey),
            url = "https://api.weather.yandex.ru/v2/informers?" +
                    "lat=${mapPoint.latitude}" +
                    "&lon=${mapPoint.longitude}"
        )
        return result.getOrNull()
            ?.toDomainWeatherData(mapPoint)
            ?.let { weatherData -> Result.success(weatherData) }
            ?: Result.failure(result.exceptionOrNull() ?: Throwable())
    }

    private fun WeatherResponseRoot.toDomainWeatherData(mapPoint: MapPoint): List<RawWeatherData> {
        val factData = RawWeatherData(
            date = now * ONE_SEC,
            mapPoint = mapPoint,
            pressure = Pressure(
                mm = fact.pressureMm,
                pa = fact.pressurePa,
            ),
            temperature = Temperature(
                avg = fact.temp,
                water = fact.tempWater
            ),
            wind = Wind(
                dir = fact.windDir?.let(WindDir::getByValue),
                gust = fact.windGust,
                speed = fact.windSpeed,
            ),
            humidity = fact.humidity,
            moonCode = null
        )
        val forecastData = this.forecast.parts
            .mapNotNull { part ->
                RawWeatherData(
                    date = (this.forecast.dateTs ?: return@mapNotNull null) * ONE_SEC,
                    mapPoint = mapPoint,
                    pressure = Pressure(
                        mm = part.pressureMm,
                        pa = part.pressurePa
                    ),
                    temperature = Temperature(
                        avg = part.tempAvg,
                        water = null
                    ),
                    wind = Wind(
                        dir = part.windDir?.let(WindDir::getByValue),
                        gust = part.windGust,
                        speed = part.windSpeed,
                    ),
                    humidity = part.humidity,
                    moonCode = this.forecast.moonCode
                )
            }
        return mutableListOf<RawWeatherData>().apply {
            add(factData)
            addAll(forecastData)
        }
    }

}