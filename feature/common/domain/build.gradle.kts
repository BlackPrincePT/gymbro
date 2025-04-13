plugins {
    alias(libs.plugins.gymbro.android.library)
}

android {
    namespace = "com.pegio.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))

    implementation(libs.javax.inject)
}