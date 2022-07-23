package dmitry.molchanov.data.di

import dmitry.molchanov.data.*
import dmitry.molchanov.db.AppDatabase
import dmitry.molchanov.db.ResultQueries
import dmitry.molchanov.db.ResultToWeatherDataQueries
import dmitry.molchanov.db.WeatherDataQueries
import dmitry.molchanov.fishingforecast.repository.*
import org.koin.core.module.Module
import org.koin.dsl.module

val dataCommonKoinModule = module {

    single<MapPointRepository> {
        MapPointRepositoryImpl(mapPointQueries = get(), profileMapper = get())
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

    single<ResultQueries> {
        get<AppDatabase>().resultQueries
    }

    single<ResultToWeatherDataQueries> {
        get<AppDatabase>().resultToWeatherDataQueries
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

    single<ResultDataRepository> {
        ResultDataRepositoryImpl(
            resultQueries = get(),
            profileMapper = get(),
            mapPointMapper = get(),
            resultToWeatherDataQueries = get(),
        )
    }

}

expect val dataPlatformModule: Module