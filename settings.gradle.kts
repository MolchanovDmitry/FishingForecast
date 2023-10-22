enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "app.cash") {
                useModule("app.cash.sqldelight:gradle-plugin:+")
            }
        }
    }

    includeBuild("build-logic")

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "FishingForecast"

include(":androidApp")
include(":domain")
include(":feature:weather-data-update")
include(":data:db")
include(":data:repository:profile")
include(":core")
include(":data:preference")
include(":data:http")
include(":data:weather-remote")
include(":feature:graph")
