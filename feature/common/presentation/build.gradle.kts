plugins {
    alias(libs.plugins.gymbro.android.compose)
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