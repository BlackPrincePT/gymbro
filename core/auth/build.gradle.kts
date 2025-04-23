import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.gymbro.android.library)
    alias(libs.plugins.gymbro.hilt)
}

android {
    namespace = "com.pegio.auth"

    buildTypes {
        defaultConfig {
            buildConfigField(
                "String",
                "DEFAULT_WEB_CLIENT_ID",
                gradleLocalProperties(rootDir, providers).getProperty("DEFAULT_WEB_CLIENT_ID")
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
}