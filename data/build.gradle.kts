plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
}

version = "1.0"

sqldelight {
    database("AppDatabase") {
        packageName = "dmitry.molchanov.db"
        sourceFolders = listOf("sqldelight")
    }
    linkSqlite = true
}

kotlin {
    android()
    sourceSets {
        val sqlDelightVersion = "1.5.3"
        val settingsVersion = "0.8.1"
        val ktorVersion = "1.6.5"
        val commonMain by getting {
            dependencies {
                implementation(project(":domain"))
                implementation(Deps.koin_core)
                implementation(Deps.coroutines_core)
                implementation(Deps.coroutines_core_serialization)
                implementation("com.russhwolf:multiplatform-settings:$settingsVersion")
                implementation("com.russhwolf:multiplatform-settings-coroutines-native-mt:$settingsVersion")
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("com.squareup.sqldelight:coroutines-extensions:${sqlDelightVersion}")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
                implementation("io.ktor:ktor-client-core:1.6.1")
                implementation("io.ktor:ktor-client-okhttp:1.6.1")
                implementation("io.ktor:ktor-client-serialization-jvm:1.6.1")
                implementation("io.ktor:ktor-client-gson:1.6.1")
                implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:1.5.3")
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}