import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "com.pegio.buildLogic"

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = libs.plugins.gymbro.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.gymbro.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = libs.plugins.gymbro.android.compose.get().pluginId
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidData") {
            id = libs.plugins.gymbro.android.data.get().pluginId
            implementationClass = "AndroidDataConventionPlugin"
        }
        register("androidDomain") {
            id = libs.plugins.gymbro.android.domain.get().pluginId
            implementationClass = "AndroidDomainConventionPlugin"
        }
        register("androidPresentation") {
            id = libs.plugins.gymbro.android.presentation.get().pluginId
            implementationClass = "AndroidPresentationConventionPlugin"
        }
        register("hilt") {
            id = libs.plugins.gymbro.hilt.get().pluginId
            implementationClass = "HiltConventionPlugin"
        }
    }
}