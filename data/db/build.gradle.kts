plugins {
    id("android-feature-setup")
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("dmitry.molchanov.db")
        }
    }
}

dependencies {
    arrayOf(
        projects.core,
        projects.domain,
        libs.koin.core,
        libs.sqldelight.android.driver,
        libs.sqldelight.coroutines.ext,
        libs.sqldelight.runtime,
    ).forEach(::implementation)
}
