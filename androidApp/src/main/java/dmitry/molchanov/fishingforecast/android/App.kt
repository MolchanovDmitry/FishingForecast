package dmitry.molchanov.fishingforecast.android

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dmitry.molchanov.db.di.dbModule
import dmitry.molchanov.domain.di.domainKoinModule
import dmitry.molchanov.fishingforecast.android.di.appKoinModule
import dmitry.molchanov.preference.di.preferenceModule
import dmitry.molchanov.repository.profile.di.profileRepositoryModule
import dmitry.molchanov.weather_remote.di.weatherRemoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAP_API_KEY)

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(
                appKoinModule,
                domainKoinModule,
                dbModule,
                profileRepositoryModule,
                preferenceModule,
                weatherRemoteModule
            )
        }
    }
}
