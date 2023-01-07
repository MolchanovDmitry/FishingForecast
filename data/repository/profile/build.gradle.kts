plugins {
    id(GradlePlugins.Id.ANDROID_LIBRARY)
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "dmitry.molchanov.profile"
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
        project(Modules.DB),
        project(Modules.DOMAIN),
        "com.russhwolf:multiplatform-settings:0.8.1",
        "com.russhwolf:multiplatform-settings-coroutines-native-mt:0.8.1"
    ).forEach(::implementation)
}