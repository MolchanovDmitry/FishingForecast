package dmitry.molchanov.ext

import com.android.build.api.dsl.LibraryExtension
import java.io.File

/**
 * Динамическое определение namespace
 */
fun org.gradle.api.Project.setupAndroidModuleNamespace(libraryExtension: LibraryExtension){
    // Динамически устанавливаем namespace
    val projectSign = ".FishingForecast."
    val modulePath = project.projectDir.toString()
        .replace(File.separator, ".")
        .replace(".${project.name}", "")
    val startProjectSignIndex = modulePath.indexOf(projectSign)

    if (startProjectSignIndex != -1) {

        val shortPath = modulePath
            .substring(startProjectSignIndex + projectSign.length)
            .replace("-", "_")

        val moduleNamespace = "dmitry.molchanov.$shortPath.${project.name.replace("-", "_")}"
            .replace("..",".")
        println("[Ext] Назначен namespace = $moduleNamespace")
        libraryExtension.namespace = moduleNamespace
    }
}
