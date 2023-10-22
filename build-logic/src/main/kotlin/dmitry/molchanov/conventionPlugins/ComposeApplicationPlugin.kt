package dmitry.molchanov.conventionPlugins

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

/**
 * Плагин конфигурации компоуза для модуля уровня приложения
 */
class ComposeApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            val libs = the<LibrariesForLibs>()

            applyPlugin(libs.plugins.kotlin.android)
            applyPlugin(libs.plugins.android.application)
            applyPlugin(libs.plugins.compose.compiler)
            applyPlugin(libs.plugins.ktlint)

            pluginManager.apply("ktlint-convention")

            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }
            }

            arrayOf(
                platform(libs.compose.bom),
                libs.bundles.compose,
            ).forEach(::implementation)
        }
}
