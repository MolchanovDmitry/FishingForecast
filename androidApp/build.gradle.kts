import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
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
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    implementation(Deps.androidx_lifecycle_runtime_ktx)

    implementation(Deps.play_services_maps)
    implementation(Deps.maps_utils)
    //implementation(Deps.maps_v3)
    implementation("com.google.maps.android:maps-ktx:3.0.0")
    //implementation("com.google.maps.android:maps-utils-ktx:3.0.0")

    implementation(Deps.compose_activity)
    implementation(Deps.compose_ui)
    implementation(Deps.compose_ui_tooling)
    implementation(Deps.compose_foundation)
    implementation(Deps.compose_material)
    implementation(Deps.compose_icons_core)
    implementation(Deps.compose_icons_extended)
    implementation(Deps.compose_view_model)
    implementation("androidx.navigation:navigation-compose:2.4.0-beta02")
    //implementation("androidx.navigation:navigation-runtime-ktx:2.3.5")
}