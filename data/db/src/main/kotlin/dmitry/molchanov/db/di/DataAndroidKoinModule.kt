package dmitry.molchanov.db.di

import dmitry.molchanov.db.AppDatabase
import dmitry.molchanov.db.DriverFactory
import dmitry.molchanov.db.ForecastSettingQueries
import dmitry.molchanov.db.ForecastSettingRepositoryImpl
import dmitry.molchanov.db.MapPointQueries
import dmitry.molchanov.db.MapPointRepositoryImpl
import dmitry.molchanov.db.ProfileQueries
import dmitry.molchanov.db.ResultDataRepositoryImpl
import dmitry.molchanov.db.WeatherDataRepositoryImpl
import dmitry.molchanov.domain.repository.ForecastSettingsRepository
import dmitry.molchanov.domain.repository.MapPointRepository
import dmitry.molchanov.domain.repository.ResultDataRepository
import dmitry.molchanov.domain.repository.WeatherDataRepository
import org.koin.dsl.module

val dbModule = module {

    single<AppDatabase> {
        AppDatabase(DriverFactory(context = get()).createDriver())
    }

    single<MapPointQueries> {
        get<AppDatabase>().mapPointQueries
    }

    single<ProfileQueries> {
        get<AppDatabase>().profileQueries
    }

    single<ForecastSettingQueries> {
        get<AppDatabase>().forecastSettingQueries
    }

    single<MapPointRepository> {
        MapPointRepositoryImpl(
            profileMapper = get(), mapPointQueries = get(),
            mapDispatcher = get()
        )
    }

    single<ForecastSettingsRepository> {
        ForecastSettingRepositoryImpl(forecastSettingQueries = get(), get())
    }

    single<WeatherDataRepository> {
        WeatherDataRepositoryImpl(
            weatherDataQueries = get<AppDatabase>().weatherDataQueries,
            mapPointRepository = get(),
            get()
        )
    }

    single<ResultDataRepository> {
        ResultDataRepositoryImpl(
            resultQueries = get<AppDatabase>().resultQueries,
            profileMapper = get(),
            mapPointMapper = get(),
            resultToWeatherDataQueries = get<AppDatabase>().resultToWeatherDataQueries,
            get()
        )
    }

}