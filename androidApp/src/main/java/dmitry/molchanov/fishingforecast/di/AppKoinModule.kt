package dmitry.molchanov.fishingforecast.di

import dmitry.molchanov.core.DispatcherDefault
import dmitry.molchanov.core.DispatcherIo
import dmitry.molchanov.domain.mapper.CommonProfileFetcher
import dmitry.molchanov.domain.model.MapPoint
import dmitry.molchanov.fishingforecast.BuildConfig
import dmitry.molchanov.fishingforecast.MainViewModel
import dmitry.molchanov.fishingforecast.WeatherStatisticViewModel
import dmitry.molchanov.fishingforecast.mapper.CommonProfileFetcherImpl
import dmitry.molchanov.fishingforecast.notifier.WeatherNotifierPresenter
import dmitry.molchanov.fishingforecast.ui.map.MapViewModel
import dmitry.molchanov.fishingforecast.ui.profile.ProfileViewModel
import dmitry.molchanov.fishingforecast.ui.result.ResultDetailViewModel
import dmitry.molchanov.fishingforecast.ui.result.ResultViewModel
import dmitry.molchanov.weather_data_update.DataUpdateViewModel
import org.koin.android.ext.koin.androidContext
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
            fetchAndSaveWeatherDataUseCase = get(),
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
            fetchAndSaveWeatherDataUseCase = get()
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
            getResultUseCase = get(),
            saveResultUseCase = inject(),
            getProfilesUseCase = inject(),
            getMapPointsUseCase = inject(),
            commonProfileFetcher = inject(),
            importSharedResultUseCase = inject(),
            getSavedWeatherDataUseCase = inject(),
            getWeatherDataByResultUseCase = inject(),
            dispatcherDefault = get(),
            dispatcherIo = get(),
            cacheDir = lazy { androidContext().cacheDir },
        )
    }

    viewModel<ResultDetailViewModel> { parameters ->
        ResultDetailViewModel(parameters.get(), get())
    }

    viewModel<DataUpdateViewModel> {
        DataUpdateViewModel(
            weatherDataRepository = get(),
            fetchAndSaveWeatherDataUseCase = get(),
            dispatcherDefault = get()
        )
    }

    single<DispatcherDefault> { DispatcherDefault() }
    single<DispatcherIo> { DispatcherIo() }
}
