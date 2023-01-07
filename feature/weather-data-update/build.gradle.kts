plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id(GradlePlugins.Id.KTLINT)
}

android {
    namespace = "dmitry.molchanov.weather_data_update"
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    listOf(
        project(Modules.DOMAIN),

        Deps.material,
        Deps.androidx_appcompat,
        Deps.Coroutines.android,

        Deps.androidx_lifecycle_runtime_ktx,
        Deps.androidx_activity_ktx,

        Deps.compose_activity,
        Deps.compose_ui,
        Deps.compose_ui_tooling,
        Deps.compose_foundation,
        Deps.compose_material,
        Deps.compose_icons_core,
        Deps.compose_icons_extended,
        Deps.compose_view_model,
        Deps.compose_navigation,

        Deps.Koin.core,
        Deps.Koin.android,
    ).forEach(::implementation)
}
