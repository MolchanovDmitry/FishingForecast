plugins {
    id("android-feature-setup")
    alias(libs.plugins.setup.compose.library)
}

dependencies {

    listOf(
        libs.compose.material,
    ).forEach(::implementation)
}
