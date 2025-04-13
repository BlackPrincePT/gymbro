plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.firestore"
}

dependencies {
    implementation(project(":core:common"))

    api(platform(libs.firebase.bom))
    api(libs.firebase.firestore)
}