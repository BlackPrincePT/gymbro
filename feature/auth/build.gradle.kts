import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.gymbro.android.feature)
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
    implementation(project(":core:uploadmanager"))

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}