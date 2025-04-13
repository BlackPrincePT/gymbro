plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.uploadmanager"
}

dependencies {
    implementation(project(":core:common"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage.ktx)

    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
}