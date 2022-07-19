package dmitry.molchanov.fishingforecast.di

import dmitry.molchanov.fishingforecast.mapper.ProfileMapper
import dmitry.molchanov.fishingforecast.usecase.*
import org.koin.dsl.module

val domainKoinModule = module {

    factory<DeleteProfileUseCase> {
        DeleteProfileUseCase(profileRepository = get())
    }

    factory<GetCurrentWeatherDataUseCase> {
        GetCurrentWeatherDataUseCase(yandexWeatherRepository = get())
    }

    factory<GetForecastUseCase> {
        GetForecastUseCase()
    }

    factory<GetMapPointsUseCase> {
        GetMapPointsUseCase(mapPointRepository = get())
    }

    factory<GetProfileForecastSettingsUseCase> {
        GetProfileForecastSettingsUseCase(forecastSettingsRepository = get())
    }

    factory<GetProfilesUseCase> {
        GetProfilesUseCase(profileRepository = get(), commonProfileFetcher = get(), profileMapper = get())
    }

    factory<GetSavedWeatherDataUseCase> {
        GetSavedWeatherDataUseCase(weatherDataRepository = get())
    }

    factory<SaveForecastSettingMarkUseCase> {
        SaveForecastSettingMarkUseCase(repository = get())
    }

    factory<SaveMapPointUseCase> {
        SaveMapPointUseCase(repository = get())
    }

    factory<SaveProfileUseCase> {
        SaveProfileUseCase(profileRepository = get())
    }

    factory<SaveWeatherDataUseCase> {
        SaveWeatherDataUseCase(repository = get())
    }

    factory<SelectProfileUseCase> {
        SelectProfileUseCase(profileRepository = get())
    }

    factory<ProfileMapper> {
        ProfileMapper(commonProfileFetcher = get())
    }

    factory<GetCurrentProfileUseCase> {
        GetCurrentProfileUseCase(profileRepository = get(), profileMapper = get())
    }

    factory<GetForecastSettingMarksUseCase> {
        GetForecastSettingMarksUseCase(repository = get())
    }

    factory<DeleteForecastSettingUseCase> {
        DeleteForecastSettingUseCase(repository = get())
    }

}