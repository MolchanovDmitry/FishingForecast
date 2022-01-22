package dmitry.molchanov.data.yandexapi

import dmitry.molchanov.data.NetworkClient
import dmitry.molchanov.data.yandexapi.entity.WeatherResponseRoot
import dmitry.molchanov.fishingforecast.model.*
import dmitry.molchanov.fishingforecast.utils.ONE_SEC

class YandexWeatherNetworkDataSource(
    private val networkClient: NetworkClient,
    private val apiKey: String,
) {

    suspend fun getYandexWeatherDate(mapPoint: MapPoint): Result<WeatherData> {
        val result = networkClient.loadData<WeatherResponseRoot>(
            headers = mapOf("X-Yandex-API-Key" to apiKey),
            url = "https://api.weather.yandex.ru/v2/informers?\n" +
                    "lat=${mapPoint.latitude}\n" +
                    "&lon=${mapPoint.longitude}"
        )
        result.getOrNull()?.let { weatherResponse ->
            WeatherData(
                date = weatherResponse.now * ONE_SEC,
                mapPoint = mapPoint,
                pressure = Pressure(
                    mm = weatherResponse.fact.pressureMm,
                    pa = weatherResponse.fact.pressurePa,
                ),
                temperature = Temperature(
                    avg = weatherResponse.fact.temp,
                    water = weatherResponse.fact.tempWater
                ),
                wind = Wind(
                    speed = weatherResponse.fact.windSpeed,
                    gust = weatherResponse.fact.windGust,
                    dir = weatherResponse.fact.windDir
                ),
                humidity = weatherResponse.fact.humidity,
            )
        }
    }

    private fun WeatherResponseRoot.toDomainWeatherData(mapPoint: MapPoint) {
        val factData = WeatherData(
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
                dir = fact.windDir,
                gust = fact.windGust,
                speed = fact.windSpeed,
            ),
            humidity = fact.humidity,
        )
        /*this.forecast.parts.map { part ->
            WeatherData(
                date = when (part.partName) {
                    "morning" -> part.
                    "day" ->
                },
            )

        }*/
    }


}