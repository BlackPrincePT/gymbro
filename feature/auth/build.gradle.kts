plugins {
    alias(libs.plugins.gymbro.android.feature)
}

android {
    namespace = "com.pegio.auth"
}

dependencies {
    implementation(project(":core:uploadmanager"))

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}