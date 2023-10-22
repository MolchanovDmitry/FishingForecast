plugins {
    id("android-feature-setup")
}

dependencies {

    arrayOf(
        libs.ktor.core,
        libs.ktor.okhttp,
        libs.ktor.interceptor,
        libs.ktor.serialization,
    ).forEach(::implementation)
}
