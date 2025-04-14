plugins {
    alias(libs.plugins.gymbro.android.data)
}

android {
    namespace = "com.pegio.data"
}

dependencies {
    implementation(project(":core:firestore"))
    implementation(project(":feature:feed:domain"))
}