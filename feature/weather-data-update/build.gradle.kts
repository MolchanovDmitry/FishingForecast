plugins {
    id("android-feature-setup")
    alias(libs.plugins.setup.compose.library)
}

dependencies {
    listOf(
        projects.core,
        projects.domain,

        libs.kotlinx.coroutines.android,
        libs.android.ui.material,
        libs.androidx.ui.appcompat,
        libs.androidx.lifecycle.runtime,
        libs.androidx.activity.ktx,

        libs.compose.activity,
        libs.compose.ui,
        libs.compose.ui.tooling,
        libs.compose.foundation,
        libs.compose.material,
        libs.compose.material.icons.core,
        libs.compose.icons.ext,
        libs.compose.viewmodel,
        libs.compose.navigation,

        libs.koin.core,
        libs.koin.compose,
        libs.koin.android,
    ).forEach(::implementation)
}
