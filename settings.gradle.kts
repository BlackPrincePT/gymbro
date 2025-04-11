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

// Feature
include(":feature:common:data")
include(":feature:common:domain")

include(":feature:auth:domain")
include(":core:firebaseauth")
