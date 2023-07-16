import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.20")
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.4")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.google.com/")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0" apply true
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
