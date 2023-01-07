package dmitry.molchanov.domain.di

import dmitry.molchanov.domain.usecase.GetCurrentProfileUseCase
import dmitry.molchanov.domain.usecase.SaveWeatherDataUseCase
import dmitry.molchanov.fishingforecast.mapper.MapPointMapper
import dmitry.molchanov.fishingforecast.mapper.ProfileMapper
import dmitry.molchanov.fishingforecast.usecase.DeleteForecastSettingUseCase
import dmitry.molchanov.fishingforecast.usecase.DeleteProfileUseCase
import dmitry.molchanov.fishingforecast.usecase.GetForecastSettingMarksUseCase
import dmitry.molchanov.fishingforecast.usecase.GetForecastUseCase
import dmitry.molchanov.fishingforecast.usecase.GetMapPointByIdUseCase
import dmitry.molchanov.fishingforecast.usecase.GetMapPointsUseCase
import dmitry.molchanov.fishingforecast.usecase.GetProfileForecastSettingsUseCase
import dmitry.molchanov.fishingforecast.usecase.GetProfilesUseCase
import dmitry.molchanov.fishingforecast.usecase.GetResultsUseCase
import dmitry.molchanov.fishingforecast.usecase.GetSavedWeatherDataUseCase
import dmitry.molchanov.fishingforecast.usecase.GetWeatherDataByResultUseCase
import dmitry.molchanov.fishingforecast.usecase.ImportSharedResultUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveForecastSettingMarkUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveMapPointUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveProfileUseCase
import dmitry.molchanov.fishingforecast.usecase.SaveResultUseCase
import dmitry.molchanov.fishingforecast.usecase.SelectProfileUseCase
import org.koin.dsl.module

val domainKoinModule = module {

    factory<DeleteProfileUseCase> {
        DeleteProfileUseCase(profileRepository = get())
    }

    factory<SaveWeatherDataUseCase> {
        SaveWeatherDataUseCase(yandexWeatherRepository = get(), weatherDataRepository = get())
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
            resultDataRepository = get(),
            weatherDataRepository = get()
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