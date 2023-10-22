import java.util.Properties

plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.setup.compose.application)
}

android {

    namespace = "dmitry.molchanov.fishingforecast"

    val properties = Properties()
    val q: File = rootProject.file("local.properties")
    properties.load(q.inputStream())
    val yandexMapApiKey = properties.getProperty("yandex.map.api.key")
    val yandexWeatherApiKey = properties.getProperty("yandex.weather.api.key")

    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "dmitry.molchanov.fishingforecast"
        minSdk = libs.versions.minSdk.get().toInt()

        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "YANDEX_WEATHER_API_KEY", yandexWeatherApiKey)
        buildConfigField("String", "YANDEX_MAP_API_KEY", yandexMapApiKey)
    }

    androidResources {
        localeFilters.addAll(listOf("en", "ru"))
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            ndk {
                abiFilters.add("arm64-v8a")
            }
        }
    }

    kotlin {
        jvmToolchain(libs.versions.javaVersion.get().toInt())
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    listOf(
        projects.core,
        projects.domain,
        projects.data.db,
        projects.feature.weatherDataUpdate,
        projects.data.repository.profile,
        projects.data.preference,
        projects.data.weatherRemote,
        projects.feature.graph,

        libs.kotlinx.coroutines.android,
        libs.android.ui.material,
        libs.androidx.ui.appcompat,
        libs.androidx.lifecycle.runtime,
        libs.androidx.activity.ktx,
        libs.androidx.work.runtime.ktx,
        libs.koin.core,
        libs.koin.compose,
        libs.compose.material,
        libs.compose.navigation,
        libs.compose.icons.ext,
        libs.compose.material.icons.core,
        libs.yandex.map,
    ).forEach(::implementation)
}
