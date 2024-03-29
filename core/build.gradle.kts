plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id(GradlePlugins.Id.KTLINT)
}

android {
    namespace = "dmitry.molchanov.core"
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    arrayOf(
        Deps.Coroutines.android,
        Deps.Sqldelight.runtime,
        Deps.Sqldelight.coroutinesExt,
        Deps.Sqldelight.android_driver,
        Deps.Ktor.core,
        Deps.Ktor.okhttp,
        Deps.Ktor.serialization,
        Deps.Ktor.interceptor,
        Deps.Koin.core,
        Deps.compose_activity,
        Deps.compose_ui,
        Deps.compose_ui_tooling,
        Deps.compose_foundation,
        Deps.compose_material,
        Deps.compose_icons_core,
        Deps.compose_icons_extended,
        Deps.compose_view_model,
        Deps.compose_navigation,
    ).forEach(::api)
}
