plugins {
    alias(libs.plugins.gymbro.android.presentation)
}

android {
    namespace = "com.pegio.presentation"
}

dependencies {
    implementation(project(":feature:workout:domain"))

}