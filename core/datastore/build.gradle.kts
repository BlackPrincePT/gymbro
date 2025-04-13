plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}