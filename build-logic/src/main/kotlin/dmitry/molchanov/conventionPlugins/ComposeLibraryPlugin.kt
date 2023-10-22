package dmitry.molchanov.conventionPlugins

import com.android.build.gradle.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

/**
 * Плагин конфигурации компоуза для модуля библиотеки
 */
class ComposeLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        val libs = the<LibrariesForLibs>()

        applyPlugin(libs.plugins.kotlin.android)
        applyPlugin(libs.plugins.android.library)
        applyPlugin(libs.plugins.compose.compiler)

        extensions.configure<LibraryExtension> {
            buildFeatures {
                compose = true
            }
        }

        setJvmToolChainVersion(libs.versions.javaVersion.get().toInt())

        arrayOf(
            platform(libs.compose.bom),
            libs.bundles.compose,
        ).forEach(::implementation)
    }
}
