package dmitry.molchanov.preference.di

import com.russhwolf.settings.ObservableSettings
import dmitry.molchanov.domain.repository.AppPreferenceRepository
import dmitry.molchanov.preference.AppPreferenceRepositoryImpl
import dmitry.molchanov.preference.AppSettings
import org.koin.dsl.module

val preferenceModule = module {

        single<AppPreferenceRepository> {
            AppPreferenceRepositoryImpl(appSettings = get(), get())
        }

        single<ObservableSettings> {
            AppSettings(get()).settings
        }
    }
