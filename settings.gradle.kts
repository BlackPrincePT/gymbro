pluginManagement {

    includeBuild("build-logic")

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

rootProject.name = "gymbro"

include(":app")

// Core
include(":core:common")
include(":core:designsystem")
include(":core:firestore")
include(":core:datastore")

// Feature
include(":feature:common:data")

include(":feature:auth:presentation")

include(":feature:splash:presentation")

include(":feature:splash:domain")
include(":feature:common:domain")
include(":feature:auth:domain")
include(":core:uploadmanager")
include(":feature:common:presentation")
