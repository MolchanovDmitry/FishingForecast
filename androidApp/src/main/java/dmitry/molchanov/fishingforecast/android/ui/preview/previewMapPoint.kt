package dmitry.molchanov.fishingforecast.android.ui.preview

import dmitry.molchanov.fishingforecast.model.*
import dmitry.molchanov.fishingforecast.utils.ONE_DAY

private val previewMapPoint = MapPoint(
    id = 0,
    name = "Сохраненная точка",
    profile = SimpleProfile("Судачок"),
    latitude = 56.858721,
    longitude = 35.917596
)

val previewResult = Result(
    id = 0,
    name = "Просто супер порыбачил",
    mapPoint = previewMapPoint
)

val previewWeatherData = listOf<WeatherData>(
    WeatherData(
        id = 0,
        date = System.currentTimeMillis(),
        mapPoint = previewMapPoint,
        pressure = Pressure(mm = 120F, pa = 130f),
        temperature = Temperature(min = 10F, avg = 20F, max = 30F, water = 20F),
        wind = Wind(speed = 1F, gust = 2F, dir = "Северо-восточный ветер"),
        humidity = 10F
    ),
    WeatherData(
        id = 1,
        date = System.currentTimeMillis() - ONE_DAY,
        mapPoint = previewMapPoint,
        pressure = Pressure(mm = 100F, pa = 110f),
        temperature = Temperature(min = 11F, avg = 21F, max = 31F, water = 21F),
        wind = Wind(speed = 2F, gust = 3F, dir = "Юго-западный ветер"),
        humidity = 4F
    )
)