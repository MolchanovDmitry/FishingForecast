package dmitry.molchanov.fishingforecast.android.di

import dmitry.molchanov.fishingforecast.android.BuildConfig
import dmitry.molchanov.fishingforecast.android.MainViewModel
import dmitry.molchanov.fishingforecast.android.WeatherStatisticViewModel
import dmitry.molchanov.fishingforecast.android.notifier.WeatherNotifierPresenter
import dmitry.molchanov.fishingforecast.android.ui.result.ResultViewModel
import dmitry.molchanov.fishingforecast.android.ui.weather.WeatherDebugViewModel
import dmitry.molchanov.fishingforecast.model.MapPoint
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appKoinModule = module {

    factory<String> {
        BuildConfig.API_KEY
    }

    factory<WeatherNotifierPresenter> {
        // TODO вынести в отдельный модуль.
        WeatherNotifierPresenter(
            getCurrentWeatherDataUseCase = get(),
            getMapPointsUseCase = get(),
            saveWeatherDataUseCase = get(),
        )
    }

    viewModel<WeatherDebugViewModel> {
        WeatherDebugViewModel()
    }

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
            getSavedWeatherData = get(),
            yandexWeatherRepository = get(),
            weatherDataRepository = get()
        )
    }

    viewModel<WeatherStatisticViewModel> { parameters ->
        WeatherStatisticViewModel(
            mapPoint = parameters.get<MapPoint>(),
            getProfilesUseCase = get(),
            getForecastUseCase = get(),
            weatherDataRepository = get(),
            getForecastSettingMarksUseCase = get()
        )
    }

    viewModel<ResultViewModel> {
        ResultViewModel(get())
    }
}