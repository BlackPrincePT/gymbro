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
include(":core:auth")
include(":core:model")
include(":core:common")
include(":core:domain")
include(":core:network")
include(":core:datastore")
include(":core:firestore")
include(":core:designsystem")
include(":core:uploadmanager")

// Feature
include(":feature:aichat")
include(":feature:auth")
include(":feature:common")
include(":feature:feed")
include(":feature:settings")
include(":feature:splash")
include(":feature:workout")
include(":core:notification")
