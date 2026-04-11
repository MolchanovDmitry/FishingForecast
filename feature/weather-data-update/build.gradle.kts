plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("io.gitlab.arturbosch.detekt")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)

    listOf(
        project(Modules.DOMAIN),

        libs.material,
        libs.androidx.appcompat,
        libs.coroutines.android,

        libs.androidx.lifecycle.runtime.ktx,
        libs.androidx.activity.ktx,

        libs.compose.activity,
        libs.compose.ui,
        libs.compose.ui.tooling,
        libs.compose.foundation,
        libs.compose.material,
        libs.compose.material.icons.core,
        libs.compose.material.icons.extended,
        libs.compose.viewmodel,
        libs.compose.navigation,

        libs.koin.core,
        libs.koin.compose,
        libs.koin.android
    ).forEach(::implementation)
}
