plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id(GradlePlugins.Id.kotlinx_serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    arrayOf(
        Deps.Koin.core,
        Deps.Coroutines.core,
        Deps.Serialization.kotlinx_core,
        Deps.Serialization.kotlinx_json,
        Deps.Kotlinx.datetime
    ).forEach(::implementation)
}