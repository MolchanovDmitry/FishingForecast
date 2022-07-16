import java.util.*

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
}

android {

    val properties = Properties()
    val q: File = rootProject.file("local.properties")
    properties.load(q.inputStream())
    val googleMapApiKey = properties.getProperty("google.map.key")
    val yandexWeatherApiKey = properties.getProperty("yandex.weather.api.key")

    compileSdk = 31
    defaultConfig {
        applicationId = "dmitry.molchanov.fishingforecast.android"
        minSdk = 24
        targetSdk = 31
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
        kotlinCompilerExtensionVersion = Deps.compose_version
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:29.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx:23.0.3")
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
        Deps.koin_compose
    ).forEach(::implementation)
}