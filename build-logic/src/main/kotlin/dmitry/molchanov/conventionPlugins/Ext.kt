package dmitry.molchanov.conventionPlugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.applyPlugin(plugin: Provider<PluginDependency>): Plugin<*> = plugins.apply(plugin.get().pluginId)

fun Project.implementation(dependency: Any) =
    dependencies.add("implementation", dependency)

fun Project.platform(dependency: Provider<MinimalExternalModuleDependency>): Provider<MinimalExternalModuleDependency> =
    dependencies.platform(dependency)

fun PluginDependenciesSpecScope.pluginId(plugin: Provider<PluginDependency>) = id(plugin.get().pluginId)

fun Project.setJvmToolChainVersion(javaVersion: Int) {
    val service = extensions.getByType<JavaToolchainService>()
    val customLauncher = service.launcherFor {
        val jdk: String by rootProject.extra
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinJavaToolchain.toolchain.use(customLauncher)
    }
}
