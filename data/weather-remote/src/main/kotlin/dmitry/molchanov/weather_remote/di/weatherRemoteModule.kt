package dmitry.molchanov.weather_remote.di

import dmitry.molchanov.domain.repository.YandexWeatherRepository
import dmitry.molchanov.weather_remote.YandexWeatherNetworkDataSourceImpl
import org.koin.dsl.module

val weatherRemoteModule = module {

    single<YandexWeatherRepository> {
        YandexWeatherNetworkDataSourceImpl(apiKey = get())
    }
}