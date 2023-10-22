plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(libs.versions.javaVersion.get().toInt())
}

dependencies {
    arrayOf(
        projects.core,
        libs.koin.core,
        libs.kotlinx.coroutines.core,
        libs.kotlinx.serialization.core,
        libs.kotlinx.serialization.json,
        libs.kotlinx.datetime
    ).forEach(::implementation)

    arrayOf(
        Deps.junit
    ).forEach(::testImplementation)
}
