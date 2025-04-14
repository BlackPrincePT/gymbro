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
include(":core:model")
include(":core:designsystem")
include(":core:datastore")
include(":core:network")
include(":core:firestore")
include(":core:uploadmanager")

// Feature
include(":feature:common:data")
include(":feature:common:domain")
include(":feature:common:presentation")

include(":feature:splash:domain")
include(":feature:splash:presentation")

include(":feature:auth:domain")
include(":feature:auth:presentation")

include(":feature:aichat:data")
include(":feature:aichat:domain")
include(":feature:aichat:presentation")

include(":feature:feed:data")
include(":feature:feed:domain")
include(":feature:feed:presentation")


include(":feature:workout:data")
include(":feature:workout:domain")
include(":feature:workout:presentation")
