package dmitry.molchanov.data.di

import dmitry.molchanov.data.MapPointRepositoryImpl
import dmitry.molchanov.data.ProfileRepositoryImpl
import dmitry.molchanov.fishingforecast.repository.MapPointRepository
import dmitry.molchanov.fishingforecast.repository.ProfileRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val dataCommonKoinModule = module {

    single<MapPointRepository> {
        MapPointRepositoryImpl(mapPointQueries = get())
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(profileQueries = get())
    }

}

expect val dataPlatformModule: Module