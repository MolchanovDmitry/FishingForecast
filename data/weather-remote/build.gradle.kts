plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id(GradlePlugins.Id.KTLINT)
    id(GradlePlugins.Id.kotlinx_serialization)
}

android {
    namespace = "dmitry.molchanov.weather_remote"
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
        project(Modules.CORE),
        project(Modules.DOMAIN),
        project(Modules.HTTP)
    ).forEach(::implementation)

    testImplementation(Deps.junit)
}
