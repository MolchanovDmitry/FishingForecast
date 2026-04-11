import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jlleitschuh.gradle.ktlint")
    id("androidx.baselineprofile") version "1.3.3"
}

android {

    namespace = "dmitry.molchanov.fishingforecast.android"

    val properties = Properties()
    val q: File = rootProject.file("local.properties")
    properties.load(q.inputStream())
    val yandexMapApiKey = properties.getProperty("yandex.map.api.key")
    val yandexWeatherApiKey = properties.getProperty("yandex.weather.api.key")

    compileSdk = Config.compileSdk
    defaultConfig {
        applicationId = "dmitry.molchanov.fishingforecast.android"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "YANDEX_WEATHER_API_KEY", yandexWeatherApiKey)
        buildConfigField("String", "YANDEX_MAP_API_KEY", yandexMapApiKey)
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            isShrinkResources = false
            isDebuggable = false
        }
    }
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Baseline Profile — генерируется в benchmark модуле
    "baselineProfile"(project(":benchmark"))

    listOf(
        project(Modules.CORE),
        project(Modules.DOMAIN),
        project(Modules.DB),
        project(Modules.WEATHER_DATA_UPDATE),
        project(Modules.PROFILE),
        project(Modules.PREFERENCE),
        project(Modules.WEATHER_REMOTE),
        project(Modules.GRAPH),

        libs.material,
        libs.androidx.appcompat,
        libs.coroutines.android,

        libs.androidx.lifecycle.runtime.ktx,
        libs.androidx.activity.ktx,

        libs.koin.core,
        libs.koin.compose,

        libs.yandex.maps,
        libs.androidx.work
    ).forEach(::implementation)
}
