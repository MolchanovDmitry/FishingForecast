package dmitry.molchanov.data.di

import dmitry.molchanov.data.*
import dmitry.molchanov.db.AppDatabase
import dmitry.molchanov.db.WeatherDataQueries
import dmitry.molchanov.fishingforecast.repository.*
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

    single<AppPreferenceRepository> {
        AppPreferenceRepositoryImpl(appSettings = get())
    }

}

expect val dataPlatformModule: Module