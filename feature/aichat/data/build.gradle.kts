plugins {
    alias(libs.plugins.gymbro.android.data)
}

android {
    namespace = "com.pegio.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:firestore"))
    implementation(project(":feature:aichat:domain"))

}