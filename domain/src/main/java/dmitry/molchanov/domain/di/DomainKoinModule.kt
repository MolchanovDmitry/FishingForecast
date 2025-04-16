package dmitry.molchanov.domain.di

import dmitry.molchanov.domain.mapper.MapPointMapper
import dmitry.molchanov.domain.mapper.ProfileMapper
import dmitry.molchanov.domain.usecase.DeleteForecastSettingUseCase
import dmitry.molchanov.domain.usecase.DeleteProfileUseCase
import dmitry.molchanov.domain.usecase.FetchAndSaveWeatherDataUseCase
import dmitry.molchanov.domain.usecase.GetCurrentProfileUseCase
import dmitry.molchanov.domain.usecase.GetForecastSettingMarksUseCase
import dmitry.molchanov.domain.usecase.GetForecastUseCase
import dmitry.molchanov.domain.usecase.GetMapPointByIdUseCase
import dmitry.molchanov.domain.usecase.GetMapPointsUseCase
import dmitry.molchanov.domain.usecase.GetProfileForecastSettingsUseCase
import dmitry.molchanov.domain.usecase.GetProfilesUseCase
import dmitry.molchanov.domain.usecase.GetResultsUseCase
import dmitry.molchanov.domain.usecase.GetSavedWeatherDataUseCase
import dmitry.molchanov.domain.usecase.GetWeatherDataByResultUseCase
import dmitry.molchanov.domain.usecase.ImportSharedResultUseCase
import dmitry.molchanov.domain.usecase.SaveForecastSettingMarkUseCase
import dmitry.molchanov.domain.usecase.SaveMapPointUseCase
import dmitry.molchanov.domain.usecase.SaveProfileUseCase
import dmitry.molchanov.domain.usecase.SaveResultUseCase
import dmitry.molchanov.domain.usecase.SelectProfileUseCase
import org.koin.dsl.module

val domainKoinModule = module {

    factory<DeleteProfileUseCase> {
        DeleteProfileUseCase(profileRepository = get())
    }

    factory<FetchAndSaveWeatherDataUseCase> {
        FetchAndSaveWeatherDataUseCase(yandexWeatherRepository = get(), weatherDataRepository = get())
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
        GetProfilesUseCase(
            profileRepository = get(), commonProfileFetcher = get(), profileMapper = get()
        )
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

    factory<SelectProfileUseCase> {
        SelectProfileUseCase(profileRepository = get())
    }

    factory<ProfileMapper> {
        ProfileMapper(commonProfileFetcher = get())
    }

    factory<MapPointMapper> {
        MapPointMapper(getMapPointsByIdUseCase = get())
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

    factory<SaveResultUseCase> {
        SaveResultUseCase(resultDataRepository = get(), weatherDataRepository = get())
    }

    factory<GetResultsUseCase> {
        GetResultsUseCase(resultDataRepository = get())
    }

    factory<GetMapPointByIdUseCase> {
        GetMapPointByIdUseCase(mapPointRepository = get())
    }

    factory<GetWeatherDataByResultUseCase> {
        GetWeatherDataByResultUseCase(
            resultDataRepository = get(), weatherDataRepository = get()
        )
    }

    factory<ImportSharedResultUseCase> {
        ImportSharedResultUseCase(
            saveResultUseCase = get(),
            saveProfileUseCase = get(),
            saveMapPointUseCase = get(),
            weatherDataRepository = get()
        )
    }
}
