plugins {
    id("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
    additionalEditorconfig.set(
        mapOf(
            "ij_kotlin_allow_trailing_comma" to "false",
            "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
        )
    )
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF)
    }
}