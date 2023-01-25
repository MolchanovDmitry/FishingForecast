pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
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
