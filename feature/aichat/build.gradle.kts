plugins {
    alias(libs.plugins.gymbro.android.feature)
}

android {
    namespace = "com.pegio.aichat"
}

dependencies {
    implementation(project(":core:uploadmanager"))

    implementation(libs.kotlinx.datetime)
}