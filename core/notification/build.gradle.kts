plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.notification"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:auth"))
    implementation(project(":core:firestore"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)
}