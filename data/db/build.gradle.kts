plugins {
    id(GradlePlugins.Id.ANDROID_LIBRARY)
    id(GradlePlugins.Id.KTLINT)
    id("org.jetbrains.kotlin.android")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("AppDatabase") {
        packageName = "dmitry.molchanov.db"
        sourceFolders = listOf("sqldelight")
    }
    linkSqlite = true
}

android {
    namespace = "dmitry.molchanov.db"
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
    ).forEach(::implementation)
}
