plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.firebaseauth"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    api(libs.firebase.auth)
}