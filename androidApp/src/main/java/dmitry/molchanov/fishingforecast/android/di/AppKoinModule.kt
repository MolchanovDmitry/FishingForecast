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
            saveProfileUseCase = get(),
            deleteProfileUseCase = inject(),
            selectProfileUseCase = inject(),
            getCurrentProfileUseCase = get(),
            getForecastSettingMarks = get(),
            deleteForecastSettings = inject(),
            saveForecastSettingMarkUseCase = inject(),
            getSavedWeatherData = get()
        )
    }
}