import dmitry.molchanov.ext.setupAndroidModuleNamespace
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.android.library")
    kotlin("android")
    id("ktlint-convention")
}

val libs = the<LibrariesForLibs>()

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    kotlin {
        jvmToolchain(libs.versions.javaVersion.get().toInt())
    }

    kotlinOptions {
        freeCompilerArgs += listOf("-Xcontext-receivers")
    }

    setupAndroidModuleNamespace(this)
}
