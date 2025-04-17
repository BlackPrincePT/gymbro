import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.pegio.network"

    buildTypes {
        defaultConfig {
            buildConfigField(
                "String",
                "GPT_BASE_URL",
                gradleLocalProperties(rootDir, providers).getProperty("GPT_BASE_URL")
            )
            buildConfigField(
                "String",
                "GPT_ENDPOINT",
                gradleLocalProperties(rootDir, providers).getProperty("GPT_ENDPOINT")
            )
            buildConfigField(
                "String",
                "API_KEY",
                gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
}