plugins {
    alias(libs.plugins.gymbro.android.feature)
}

android {
    namespace = "com.pegio.auth"
}

dependencies {
    implementation(project(":core:uploadmanager"))
}