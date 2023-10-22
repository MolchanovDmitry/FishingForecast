plugins {
    id("android-feature-setup")
}

dependencies {
    arrayOf(
        projects.domain,
        libs.koin.core,
        libs.multiplatform.settings,
        libs.multiplatform.settings.coroutines.native.mt,
    ).forEach(::implementation)

}
