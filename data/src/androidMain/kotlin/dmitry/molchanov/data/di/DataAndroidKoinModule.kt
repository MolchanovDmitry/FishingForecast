package dmitry.molchanov.data.di

import dmitry.molchanov.data.DriverFactory
import dmitry.molchanov.db.AppDatabase
import dmitry.molchanov.db.MapPointQueries
import dmitry.molchanov.db.ProfileQueries
import org.koin.dsl.module

actual val dataPlatformModule = module {

    single<AppDatabase> {
        AppDatabase(DriverFactory(context = get()).createDriver())
    }

    single<MapPointQueries> {
        get<AppDatabase>().mapPointQueries
    }

    single<ProfileQueries> {
        get<AppDatabase>().profileQueries
    }

}