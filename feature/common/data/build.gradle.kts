plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.data"
}

dependencies {
    implementation(project(":core:firestore"))
    implementation(project(":core:firebaseauth"))

    api(project(":feature:common:domain"))
}