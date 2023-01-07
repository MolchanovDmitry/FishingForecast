package dmitry.molchanov.repository.profile.di

import dmitry.molchanov.domain.repository.ProfileRepository
import dmitry.molchanov.repository.profile.ProfileRepositoryImpl
import org.koin.dsl.module

val profileRepositoryModule = module {

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            profileQueries = get(),
            appSettings = get()
        )
    }
}