plugins {
    alias(libs.plugins.gymbro.android.feature)
}

android {
    namespace = "com.pegio.settings"
}

dependencies {
    implementation(project(":core:uploadmanager"))
}