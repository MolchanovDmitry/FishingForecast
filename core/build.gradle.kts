plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("io.gitlab.arturbosch.detekt")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    api(composeBom)

    arrayOf(
        libs.coroutines.android,
        libs.sqldelight.android.driver,
        libs.sqldelight.coroutines.ext,
        libs.ktor.client.core,
        libs.ktor.client.okhttp,
        libs.ktor.client.content.negotiation,
        libs.ktor.serialization.kotlinx.json,
        libs.ktor.client.logging,
        libs.okhttp,
        libs.okhttp.logging.interceptor,
        libs.koin.core,
        libs.compose.activity,
        libs.compose.ui,
        libs.compose.ui.tooling,
        libs.compose.foundation,
        libs.compose.material,
        libs.compose.material.icons.core,
        libs.compose.material.icons.extended,
        libs.compose.viewmodel,
        libs.compose.navigation
    ).forEach(::api)
}
