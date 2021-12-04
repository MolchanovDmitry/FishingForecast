plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "dmitry.molchanov.fishingforecast.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
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
    implementation(project(":domain"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    implementation(Deps.compose_activity)
    implementation(Deps.compose_ui)
    implementation(Deps.compose_ui_tooling)
    implementation(Deps.compose_foundation)
    implementation(Deps.compose_material)
    implementation(Deps.compose_icons_core)
    implementation(Deps.compose_icons_extended)
    implementation(Deps.compose_view_model)
}