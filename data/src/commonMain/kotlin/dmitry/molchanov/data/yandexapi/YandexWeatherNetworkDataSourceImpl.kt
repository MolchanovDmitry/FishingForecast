package dmitry.molchanov.data.yandexapi

import dmitry.molchanov.data.NetworkClient
import dmitry.molchanov.data.yandexapi.entity.WeatherResponseRoot
import dmitry.molchanov.fishingforecast.model.*
import dmitry.molchanov.fishingforecast.repository.YandexWeatherRepository
import dmitry.molchanov.fishingforecast.utils.ONE_SEC

class YandexWeatherNetworkDataSourceImpl(
    private val apiKey: String,
): YandexWeatherRepository {

    override suspend fun getYandexWeatherDate(mapPoint: MapPoint): Result<List<WeatherData>> {
        val result = NetworkClient.loadData<WeatherResponseRoot>(
            headers = mapOf("X-Yandex-API-Key" to apiKey),
            url = "https://api.weather.yandex.ru/v2/informers?\n" +
                    "lat=${mapPoint.latitude}\n" +
                    "&lon=${mapPoint.longitude}"
        )
        return result.getOrNull()
            ?.toDomainWeatherData(mapPoint)
            ?.let { Result.success(it) }
            ?: Result.failure(result.exceptionOrNull() ?: Throwable())
    }

    private fun WeatherResponseRoot.toDomainWeatherData(mapPoint: MapPoint): List<WeatherData> {
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
        return listOf(factData) // TODO добавить промежуточных значений.
    }

}