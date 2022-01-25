import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

val properties = Properties()
val q: File = rootProject.file("local.properties")
properties.load(q.inputStream())
val googleMapApiKey = properties.getProperty("google.map.key")

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "dmitry.molchanov.fishingforecast.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["googleMapApiKey"] = googleMapApiKey
        buildConfigField("String", "API_KEY", properties.getProperty("yandex.weather.api.key"))
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Deps.compose_version
    }
}

dependencies {
    listOf(
        project(":data"),
        project(":domain"),

        Deps.material,
        Deps.androidx_appcompat,
        Deps.coroutines_android,

        Deps.androidx_lifecycle_runtime_ktx,

        Deps.play_services_maps,
        Deps.maps_utils,
        Deps.maps_ktx,

        Deps.compose_activity,
        Deps.compose_ui,
        Deps.compose_ui_tooling,
        Deps.compose_foundation,
        Deps.compose_material,
        Deps.compose_icons_core,
        Deps.compose_icons_extended,
        Deps.compose_view_model,
        Deps.compose_navigation,

        Deps.plot,
        Deps.koin_core,
        Deps.koin_android,
    ).forEach(::implementation)
}