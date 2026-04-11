import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

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
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply true
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.SARIF)
    }
}
