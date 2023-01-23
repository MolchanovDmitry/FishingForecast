import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id(GradlePlugins.Id.KTLINT)
}

android {

    val properties = Properties()
    val q: File = rootProject.file("local.properties")
    properties.load(q.inputStream())
    val googleMapApiKey = properties.getProperty("google.map.key")
    val yandexWeatherApiKey = properties.getProperty("yandex.weather.api.key")

    compileSdk = Config.compileSdk
    defaultConfig {
        applicationId = "dmitry.molchanov.fishingforecast.android"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["googleMapApiKey"] = googleMapApiKey
        buildConfigField("String", "API_KEY", yandexWeatherApiKey)
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    listOf(
        project(Modules.CORE),
        project(Modules.DOMAIN),
        project(Modules.DB),
        project(Modules.WEATHER_DATA_UPDATE),
        project(Modules.PROFILE),
        project(Modules.PREFERENCE),
        project(Modules.WEATHER_REMOTE),
        project(Modules.GRAPH),

        Deps.material,
        Deps.androidx_appcompat,
        Deps.Coroutines.android,

        Deps.androidx_lifecycle_runtime_ktx,
        Deps.androidx_activity_ktx,

        Deps.play_services_maps,
        Deps.maps_utils,
        Deps.maps_ktx,

        Deps.Koin.core,
        Deps.Koin.compose,
        Deps.maps_compose,
    ).forEach(::implementation)
}
