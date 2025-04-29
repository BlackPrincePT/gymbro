plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.pegio.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(project(":core:auth"))
    implementation(project(":core:network"))
    implementation(project(":core:firestore"))

    implementation(project(":core:datastore"))
    implementation(project(":core:uploadmanager"))

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)
}