plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.firestore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    api(platform(libs.firebase.bom))
    api(libs.firebase.firestore)
}