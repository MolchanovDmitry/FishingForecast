plugins {
    id("android-feature-setup")
}

dependencies {
    arrayOf(
        projects.core,
        projects.data.db,
        projects.domain,
        libs.kotlinx.coroutines.core,
        libs.sqldelight.coroutines.ext,
        libs.koin.core,
        libs.multiplatform.settings,
        libs.multiplatform.settings.coroutines.native.mt,
    ).forEach(::implementation)
}
