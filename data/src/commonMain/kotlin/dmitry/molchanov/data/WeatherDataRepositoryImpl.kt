package dmitry.molchanov.data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dmitry.molchanov.db.WeatherDataQueries
import dmitry.molchanov.fishingforecast.model.Pressure
import dmitry.molchanov.fishingforecast.model.Temperature
import dmitry.molchanov.fishingforecast.model.Wind
import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dmitry.molchanov.db.WeatherData as DataWeatherData
import dmitry.molchanov.fishingforecast.model.WeatherData as DomainWeatherData

class WeatherDataRepositoryImpl(
    private val weatherDataQueries: WeatherDataQueries,
    private val mapPointRepository: MapPointRepository
) : WeatherDataRepository {

    override suspend fun fetchWeatherData(
        /*mapPoint: MapPoint,
        from: TimeMs,
        to: TimeMs*/
    ): Flow<List<DomainWeatherData>> {
        return weatherDataQueries.selectAll()
            .asFlow()
            .mapToList()
            .map { it.mapNotNull { dataWeatherData -> getDomainWeatherData(dataWeatherData) } }
    }

    override suspend fun saveWeatherData(weatherData: List<DomainWeatherData>) {
        weatherData
            .map(::getDataWeatherData)
            .forEach(weatherDataQueries::insert)
    }

    private fun getDataWeatherData(domainWeatherData: DomainWeatherData): DataWeatherData =
        DataWeatherData(
            mapPointId = domainWeatherData.mapPoint.id,
            date = domainWeatherData.date,
            tempAvg = domainWeatherData.temperature?.avg?.toDouble(),
            tempWater = domainWeatherData.temperature?.water?.toDouble(),
            windSpeed = domainWeatherData.wind?.speed?.toDouble(),
            windGust = domainWeatherData.wind?.gust?.toDouble(),
            windDir = domainWeatherData.wind?.dir,
            pressureMm = domainWeatherData.pressure?.mm?.toDouble(),
            pressurePa = domainWeatherData.pressure?.pa?.toDouble(),
            humidity = domainWeatherData.humidity?.toDouble()
        )

    private suspend fun getDomainWeatherData(dataWeatherData: DataWeatherData): DomainWeatherData? {
        val mapPoint = mapPointRepository.getMapPoint(dataWeatherData.mapPointId) ?: return null
        return DomainWeatherData(
            date = dataWeatherData.date,
            mapPoint = mapPoint,
            pressure = Pressure(
                mm = dataWeatherData.pressureMm?.toFloat(),
                pa = dataWeatherData.pressurePa?.toFloat()
            ),
            temperature = Temperature(
                avg = dataWeatherData.tempAvg?.toFloat()
            ),
            wind = Wind(
                speed = dataWeatherData.windSpeed?.toFloat(),
                gust = dataWeatherData.windGust?.toFloat(),
                dir = dataWeatherData.windDir
            ),
            humidity = dataWeatherData.humidity?.toFloat()
        )
    }
}