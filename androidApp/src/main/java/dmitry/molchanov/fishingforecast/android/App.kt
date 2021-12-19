package dmitry.molchanov.fishingforecast.android

import android.app.Application
import dmitry.molchanov.data.di.dataCommonKoinModule
import dmitry.molchanov.data.di.dataPlatformModule
import dmitry.molchanov.fishingforecast.android.di.appKoinModule
import dmitry.molchanov.fishingforecast.di.domainKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(
                appKoinModule,
                domainKoinModule,
                dataPlatformModule,
                dataCommonKoinModule,
            )
        }
    }
}