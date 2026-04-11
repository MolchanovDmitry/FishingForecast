pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.plugin.compose" ->
                    useModule(
                        "org.jetbrains.kotlin:compose-compiler-gradle-plugin:${requested.version}"
                    )

                "androidx.baselineprofile" -> {
                    val artifact =
                        "androidx.benchmark:benchmark-baseline-profile-gradle-plugin"
                    useModule("$artifact:${requested.version}")
                }

                "com.android.test" ->
                    useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FishingForecast"
include(":androidApp")
include(":benchmark")
include(":domain")
include(":feature:weather-data-update")
include(":data:db")
include(":data:repository:profile")
include(":core")
include(":data:preference")
include(":data:http")
include(":data:weather-remote")
include(":feature:graph")
