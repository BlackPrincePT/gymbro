plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:auth"))
    implementation(project(":core:firestore"))
    implementation(project(":core:network"))
}