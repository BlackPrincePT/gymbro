// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.ksp) apply false

    // Module
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false

    // Serialization
    alias(libs.plugins.kotlin.serialization) apply false

    // Hilt
    alias(libs.plugins.dagger.hilt.android) apply false

    // Firebase
    alias(libs.plugins.google.services) apply false
}