plugins {
    alias(libs.plugins.gymbro.android.compose)
}

android {
    namespace = "com.pegio.designsystem"
}

dependencies {
    api(libs.androidx.material3)
    api(libs.androidx.material.icons.extended)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}