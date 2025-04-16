package dmitry.molchanov.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dmitry.molchanov.core.DispatcherDefault
import dmitry.molchanov.core.DispatcherIo
import dmitry.molchanov.domain.mapper.toWeatherDate
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.Pressure
import dmitry.molchanov.domain.model.RawWeatherData
import dmitry.molchanov.domain.model.Temperature
import dmitry.molchanov.domain.model.Wind
import dmitry.molchanov.domain.model.WindDir
import dmitry.molchanov.domain.repository.MapPointRepository
import dmitry.molchanov.domain.repository.WeatherDataRepository
import dmitry.molchanov.domain.utils.TimeMs
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dmitry.molchanov.db.WeatherData as DataWeatherData
import dmitry.molchanov.domain.model.WeatherData as DomainWeatherData

class WeatherDataRepositoryImpl(
    private val weatherDataQueries: WeatherDataQueries,
    private val mapPointRepository: MapPointRepository,
    private val dispatcherDefault: DispatcherDefault,
    private val dispatcherIo: DispatcherIo,
) : WeatherDataRepository {

    override fun fetchAllWeatherData(): Flow<List<DomainWeatherData>> {
        return weatherDataQueries.selectAll()
            .asFlow()
            .flowOn(dispatcherIo)
            .mapToList(dispatcherDefault)
            .map { weatherData ->
                weatherData.mapNotNull { dataWeatherData -> getDomainWeatherData(dataWeatherData) }
            }.flowOn(dispatcherDefault)
    }

    override suspend fun getLastWeatherData(): DomainWeatherData? = withContext(dispatcherIo) {
        weatherDataQueries.getLast()
            .executeAsOneOrNull()
            ?.let { weatherData -> getDomainWeatherData(weatherData) }
    }

    override fun fetchWeatherDataFlow(
        mapPoint: MapPoint,
        from: TimeMs,
        to: TimeMs,
    ): Flow<List<DomainWeatherData>> {
        return weatherDataQueries.selectAll()// TODO запрашивать по параметрам
            .asFlow()
            .flowOn(dispatcherIo)
            .mapToList(dispatcherDefault)
            .map { weatherData ->
                weatherData.filter { it.mapPointId == mapPoint.id && it.date <= to && it.date >= from }
            }
            .map { weatherData ->
                weatherData.mapNotNull { dataWeatherData -> getDomainWeatherData(dataWeatherData) }
            }.flowOn(dispatcherDefault)
    }

    /**
     * Сохраняем фактические погодные данные.
     * 1. Сначала пробуем получить уже имеющууся запись из базы
     * 2. Затем перезаписываем фактические данные поверх сохраненных
     */
    override suspend fun saveRawWeatherData(weatherData: List<RawWeatherData>) =
        withContext(dispatcherIo) {
            coroutineScope {
                weatherData
                    .forEach { weatherDataItem ->
                        launch {
                            val roundedWeatherDate =
                                weatherDataItem.date.toWeatherDate().roundedValue
                            val savedWeatherDataItem = weatherDataQueries.get(
                                date = roundedWeatherDate,
                                mapPointId = weatherDataItem.mapPoint.id
                            ).executeAsOneOrNull()

                            weatherDataQueries.insert(
                                mapPointId = weatherDataItem.mapPoint.id,
                                date = roundedWeatherDate,
                                tempAvg = weatherDataItem.temperature?.avg?.toDouble()
                                    ?: savedWeatherDataItem?.tempAvg,
                                tempWater = weatherDataItem.temperature?.water?.toDouble()
                                    ?: savedWeatherDataItem?.tempWater,
                                windSpeed = weatherDataItem.wind?.speed?.toDouble()
                                    ?: savedWeatherDataItem?.windSpeed,
                                windGust = weatherDataItem.wind?.gust?.toDouble()
                                    ?: savedWeatherDataItem?.windGust,
                                windDir = weatherDataItem.wind?.dir?.value
                                    ?: savedWeatherDataItem?.windDir,
                                pressureMm = weatherDataItem.pressure?.mm?.toDouble()
                                    ?: savedWeatherDataItem?.pressureMm,
                                pressurePa = weatherDataItem.pressure?.pa?.toDouble()
                                    ?: savedWeatherDataItem?.pressurePa,
                                humidity = weatherDataItem.humidity?.toDouble()
                                    ?: savedWeatherDataItem?.humidity,
                                moonCode = weatherDataItem.moonCode?.toLong()
                                    ?: savedWeatherDataItem?.moonCode
                            )
                        }
                    }
            }
        }

    override suspend fun saveWeatherData(weatherData: List<DomainWeatherData>) =
        withContext(dispatcherIo) {
            weatherData
                .forEach { weatherDataItem ->
                    weatherDataQueries.insert(
                        mapPointId = weatherDataItem.mapPoint.id,
                        date = weatherDataItem.date.roundedValue,
                        tempAvg = weatherDataItem.temperature?.avg?.toDouble(),
                        tempWater = weatherDataItem.temperature?.water?.toDouble(),
                        windSpeed = weatherDataItem.wind?.speed?.toDouble(),
                        windGust = weatherDataItem.wind?.gust?.toDouble(),
                        windDir = weatherDataItem.wind?.dir?.value,
                        pressureMm = weatherDataItem.pressure?.mm?.toDouble(),
                        pressurePa = weatherDataItem.pressure?.pa?.toDouble(),
                        humidity = weatherDataItem.humidity?.toDouble(),
                        moonCode = weatherDataItem.moonCode?.toLong()
                    )
                }
        }

    override suspend fun fetchWeatherData(
        mapPoint: MapPoint,
        from: TimeMs,
        to: TimeMs,
    ): List<DomainWeatherData> = withContext(dispatcherIo) {
        weatherDataQueries.selectAll()// TODO запрашивать по параметрам
            .executeAsList()
            .filter {
                it.mapPointId == mapPoint.id && it.date <= to && it.date >= from
            }
            .mapNotNull { weatherData ->
                getDomainWeatherData(weatherData)
            }
    }

    override suspend fun getWeatherDataIds(weatherData: List<DomainWeatherData>): List<Long> =
        withContext(dispatcherIo) {
            val mapPointsIds = weatherData.map { it.mapPoint.id }
            val dates = weatherData.map { it.date }
            weatherDataQueries.selectAll()
                .executeAsList()
                .filter { mapPointsIds.contains(it.mapPointId) && dates.contains(it.date.toWeatherDate()) }
                .map { it.id }
        }

    override suspend fun getWeatherDataByIds(ids: List<Long>) = withContext(dispatcherIo) {
        weatherDataQueries.selectByIds(ids)
            .executeAsList()
            .mapNotNull { weatherData -> getDomainWeatherData(weatherData) }
    }

    private suspend fun getDomainWeatherData(dataWeatherData: DataWeatherData): DomainWeatherData? =
        withContext(dispatcherIo) {
            val mapPoint = mapPointRepository.getMapPoint(dataWeatherData.mapPointId)
                ?: return@withContext null

            DomainWeatherData(
                id = dataWeatherData.id,
                date = dataWeatherData.date.toWeatherDate(),
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
                    dir = dataWeatherData.windDir?.let(WindDir::getByValue)
                ),
                humidity = dataWeatherData.humidity?.toFloat(),
                moonCode = dataWeatherData.moonCode?.toInt()
            )
        }
}