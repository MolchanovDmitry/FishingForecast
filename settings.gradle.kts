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
include(":data")
