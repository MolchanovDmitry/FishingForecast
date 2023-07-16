package dmitry.molchanov.fishingforecast.android.di

import dmitry.molchanov.domain.mapper.CommonProfileFetcher
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.fishingforecast.android.BuildConfig
import dmitry.molchanov.fishingforecast.android.MainViewModel
import dmitry.molchanov.fishingforecast.android.WeatherStatisticViewModel
import dmitry.molchanov.fishingforecast.android.mapper.CommonProfileFetcherImpl
import dmitry.molchanov.fishingforecast.android.notifier.WeatherNotifierPresenter
import dmitry.molchanov.fishingforecast.android.ui.map.MapViewModel
import dmitry.molchanov.fishingforecast.android.ui.profile.ProfileViewModel
import dmitry.molchanov.fishingforecast.android.ui.result.ResultDetailViewModel
import dmitry.molchanov.fishingforecast.android.ui.result.ResultViewModel
import dmitry.molchanov.weather_data_update.DataUpdateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appKoinModule = module {

    factory<String> {
        BuildConfig.YANDEX_WEATHER_API_KEY
    }

    factory<CommonProfileFetcherImpl> { CommonProfileFetcherImpl(get()) }

    factory<WeatherNotifierPresenter> {
        // TODO вынести в отдельный модуль.
        WeatherNotifierPresenter(
            getCurrentWeatherDataUseCase = get(),
            getMapPointsUseCase = get(),
            saveWeatherDataUseCase = get(),
        )
    }

    factory<CommonProfileFetcher> {
        CommonProfileFetcherImpl(get())
    }

    viewModel<MapViewModel> {
        MapViewModel(get(), get(), get(), get(), inject())
    }

    viewModel<ProfileViewModel> {
        ProfileViewModel(get(), get(), get(), inject(), inject(), inject())
    }

    viewModel<MainViewModel> {
        MainViewModel(
            getMapPointsUseCase = get(),
            getCurrentProfileUseCase = get(),
            getForecastSettingMarks = get(),
            deleteForecastSettings = inject(),
            saveForecastSettingMarkUseCase = inject(),
            yandexWeatherRepository = get(),
            weatherDataRepository = get(),
            commonProfileFetcher = get(),
            saveWeatherDataUseCase = get()
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
        ResultViewModel(
            context = get(),
            getResultUseCase = get(),
            saveResultUseCase = inject(),
            getProfilesUseCase = inject(),
            getMapPointsUseCase = inject(),
            commonProfileFetcher = inject(),
            importSharedResultUseCase = inject(),
            getSavedWeatherDataUseCase = inject(),
            getWeatherDataByResultUseCase = inject()
        )
    }

    viewModel<ResultDetailViewModel> { parameters ->
        ResultDetailViewModel(parameters.get(), get())
    }

    viewModel<DataUpdateViewModel> {
        DataUpdateViewModel(weatherDataRepository = get(), saveWeatherDataUseCase = get())
    }
}
