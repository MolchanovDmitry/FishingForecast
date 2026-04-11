plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.squareup.sqldelight")
    id("io.gitlab.arturbosch.detekt")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    arrayOf(
        project(Modules.CORE),
        project(Modules.DOMAIN)
    ).forEach(::implementation)
}
