package dmitry.molchanov.data.di

import dmitry.molchanov.data.ForecastSettingRepositoryImpl
import dmitry.molchanov.data.MapPointRepositoryImpl
import dmitry.molchanov.data.ProfileRepositoryImpl
import dmitry.molchanov.data.WeatherDataRepositoryImpl
import dmitry.molchanov.db.AppDatabase
import dmitry.molchanov.db.WeatherDataQueries
import dmitry.molchanov.fishingforecast.repository.ForecastSettingsRepository
import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import dmitry.molchanov.fishingforecast.repository.WeatherDataRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val dataCommonKoinModule = module {

    single<MapPointRepository> {
        MapPointRepositoryImpl(mapPointQueries = get())
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(profileQueries = get(), appSettings = get())
    }

    single<ForecastSettingsRepository> {
        ForecastSettingRepositoryImpl(forecastSettingQueries = get())
    }

    single<WeatherDataQueries> {
        get<AppDatabase>().weatherDataQueries
    }

    single<WeatherDataRepository> {
        WeatherDataRepositoryImpl(
            weatherDataQueries = get(),
            mapPointRepository = get()
        )
    }

}

expect val dataPlatformModule: Module