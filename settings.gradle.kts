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
        maven("https://jitpack.io")
    }
}

rootProject.name = "APIEAndroid"
include(":app")

include(":components:common")
include(":components:network")
include(":components:common_model")
include(":components:data_loader")

include(":feature_base:apie_data_loader")
include(":feature_base:apie_network")

include(":feature:home_page")
include(":feature:account")
include(":feature_base:gold_manage")
