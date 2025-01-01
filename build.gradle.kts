// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.jetbrains.kotlin.kapt) apply false
}

subprojects {
    afterEvaluate {
        extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
            val versionCatalog = project.extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

            defaultConfig {
                versionCode = versionCatalog.findVersion("versionCode").get().requiredVersion.toInt()
                versionName = versionCatalog.findVersion("versionName").get().requiredVersion
            }
        }
    }
}