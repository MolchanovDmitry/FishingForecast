package dmitry.molchanov.fishingforecast.android.di

import dmitry.molchanov.fishingforecast.android.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appKoinModule = module {

    viewModel<MainViewModel> {
        MainViewModel(
            saveMapPointUseCase = get(),
            getMapPointsUseCase = get(),
            getProfilesUseCase = get(),
            //saveProfileUseCase = get(),
            deleteProfileUseCase = get(),
            selectProfileUseCase = get(),
            //getCurrentProfileUseCase = get()
        )
    }
}