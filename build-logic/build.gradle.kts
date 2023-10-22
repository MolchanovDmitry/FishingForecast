plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // для использования version catalog в precompiled скриптах(/src/main/kotlin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.2.0") // Укажите нужную версию
    implementation(libs.gradle.plugin)
    implementation(libs.kotlin.plugin)
}

gradlePlugin {
    plugins {
        val localGradlePluginVersion = libs.versions.localGradlePluginVersion.get()
        register("ComposeApplicationPlugin") {
            id = "ComposeApplicationPlugin"
            version = localGradlePluginVersion
            implementationClass = "dmitry.molchanov.conventionPlugins.ComposeApplicationPlugin"
        }
        register("ComposeLibraryPlugin") {
            id = "ComposeLibraryPlugin"
            version = localGradlePluginVersion
            implementationClass = "dmitry.molchanov.conventionPlugins.ComposeLibraryPlugin"
        }
    }
}
