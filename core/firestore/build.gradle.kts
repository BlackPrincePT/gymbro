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
    implementation(project(":core:domain"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
}