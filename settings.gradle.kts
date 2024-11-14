pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "APIEAndroid"
include(":app")
include(":feature")

include(":components:common")
include(":components:network")
include(":components:common_model")
include(":components:data_loader")

include(":feature_base:apie_data_loader")
include(":feature_base:apie_network")
