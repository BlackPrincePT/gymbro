plugins {
    alias(libs.plugins.gymbro.android.compose)
}

android {
    namespace = "com.pegio.common"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))

    implementation(libs.javax.inject)
    implementation(libs.androidx.hilt.navigation.compose)
}