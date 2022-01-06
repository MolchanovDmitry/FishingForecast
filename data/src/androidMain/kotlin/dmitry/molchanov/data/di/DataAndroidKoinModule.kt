package dmitry.molchanov.data.di

import com.russhwolf.settings.ObservableSettings
import dmitry.molchanov.data.AppSettings
import dmitry.molchanov.data.DriverFactory
import dmitry.molchanov.db.AppDatabase
import dmitry.molchanov.db.ForecastSettingQueries
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

    single<ForecastSettingQueries>{
        get<AppDatabase>().forecastSettingQueries
    }

    single<ObservableSettings> {
        AppSettings(context = get()).settings
    }

}