plugins {
    id("android-feature-setup")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    arrayOf(
        projects.domain,
        projects.data.http,
        libs.koin.core,
        libs.kotlinx.serialization.core,
    ).forEach(::implementation)

    testImplementation(Deps.junit)
}
