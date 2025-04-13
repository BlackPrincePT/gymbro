plugins {
    alias(libs.plugins.gymbro.android.library)
}

android {
    namespace = "com.pegio.presentation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:common:domain"))

    implementation(libs.javax.inject)
}