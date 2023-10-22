plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(libs.versions.javaVersion.get().toInt())
}

dependencies {
    arrayOf(
        libs.kotlinx.coroutines.core
    ).forEach(::implementation)
}
