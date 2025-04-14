plugins {
    alias(libs.plugins.gymbro.android.presentation)
}

android {
    namespace = "com.pegio.presentation"
}

dependencies {
    implementation(project(":feature:feed:domain"))

    implementation(libs.kotlinx.datetime)
}