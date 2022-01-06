package dmitry.molchanov.data.di

import dmitry.molchanov.data.ForecastSettingRepositoryImpl
import dmitry.molchanov.data.MapPointRepositoryImpl
import dmitry.molchanov.data.ProfileRepositoryImpl
import dmitry.molchanov.fishingforecast.repository.ForecastSettingsRepository
import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
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

}

expect val dataPlatformModule: Module