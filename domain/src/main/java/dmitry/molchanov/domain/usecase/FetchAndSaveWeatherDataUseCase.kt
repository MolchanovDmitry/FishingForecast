package dmitry.molchanov.domain.usecase

import dmitry.molchanov.domain.ioDispatcher
import dmitry.molchanov.domain.mapper.toWeatherDate
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.domain.model.WeatherDate
import dmitry.molchanov.domain.repository.WeatherDataRepository
import dmitry.molchanov.domain.repository.YandexWeatherRepository
import kotlinx.coroutines.withContext

/**
 * Получить данные о погоде из яндекс репозитория и сохранить в базе
 */
class FetchAndSaveWeatherDataUseCase(
    private val weatherDataRepository: WeatherDataRepository,
    private val yandexWeatherRepository: YandexWeatherRepository
) {

    suspend fun execute(mapPoint: MapPoint): Result<WeatherDataUpdate> = withContext(ioDispatcher) {
        val currentDate = System.currentTimeMillis().toWeatherDate()
        val lastWeatherDate = weatherDataRepository.getLastWeatherData()?.date

        if (lastWeatherDate?.isSameDay(currentDate) == true) {
            return@withContext Result.failure(WeatherDataAlreadyExistError())
        }

        yandexWeatherRepository.getYandexWeatherDate(mapPoint)
            .mapCatching { weatherDataFromYandex ->
                weatherDataRepository.saveRawWeatherData(weatherDataFromYandex)
                weatherDataFromYandex.maxByOrNull { it.date }
                    ?.date
                    ?.toWeatherDate()
                    ?.let(::WeatherDataUpdate)
                    ?: error("")
            }
    }

    private fun WeatherDate.isSameDay(weatherDate: WeatherDate): Boolean =
        this.day == weatherDate.day
                && this.month == weatherDate.month
                && this.year == weatherDate.year
}

class WeatherDataAlreadyExistError :
    Throwable(message = "Погодные данные за данный день уже присутвуют")

class WeatherDataUpdate(val lastWeatherDataDate: WeatherDate)
