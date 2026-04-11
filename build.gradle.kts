import io.gitlab.arturbosch.detekt.Detekt

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.plugin.kotlin)
        classpath(libs.gradle.plugin.kotlin.serialization)
        classpath(libs.gradle.plugin.android)
        classpath(libs.gradle.plugin.sqldelight)
        classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.1.0")
    }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.6" apply true
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    ignoreFailures = false
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
    languageVersion = "1.9"
    parallel = true
    
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
    }
}
