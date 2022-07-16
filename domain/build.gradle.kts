plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

version = "1.0"

kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.koin_core)
                implementation(Deps.coroutines_core)
                implementation(Deps.coroutines_core_serialization)
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation(Deps.junit)
                implementation(Deps.mockito_core)
                implementation(Deps.mockito_inline)
                implementation(Deps.coroutine_test)
            }
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }
}