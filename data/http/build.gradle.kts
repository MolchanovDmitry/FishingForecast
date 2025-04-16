plugins {
    id("android-feature-setup")
}

dependencies {

    arrayOf(
        libs.ktor.core,
        libs.ktor.okhttp,
        libs.ktor.interceptor,
        libs.ktor.negotiation,
        libs.ktor.serialization,
        libs.ktor.client.logging,
        libs.ktor.serialization.json,
    ).forEach(::implementation)
}
