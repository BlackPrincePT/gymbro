import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)

    alias(libs.plugins.kotlin.serialization)

    // Hilt
    alias(libs.plugins.gymbro.hilt)

    // Firebase
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.pegio.gymbro"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pegio.gymbro"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "GEMINI_URL", "\"https://api.openai.com/\"")
        buildConfigField("String", "GPT_ENDPOINT", "\"v1/chat/completions\"")
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "API_KEY",
                gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
            )

            buildConfigField(
                "String",
                "DEFAULT_WEB_CLIENT_ID",
                gradleLocalProperties(rootDir, providers).getProperty("DEFAULT_WEB_CLIENT_ID")
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))


    implementation(project(":feature:common:data"))
    implementation(project(":feature:aichat:data"))
    implementation(project(":feature:feed:data"))
    implementation(project(":feature:settings:data"))
    implementation(project(":feature:workout:data"))

    implementation(project(":feature:common:presentation"))
    implementation(project(":feature:aichat:presentation"))
    implementation(project(":feature:auth:presentation"))
    implementation(project(":feature:feed:presentation"))
    implementation(project(":feature:settings:presentation"))
    implementation(project(":feature:splash:presentation"))
    implementation(project(":feature:workout:presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.kotlinx.serialization.json)

    // Navigation
    implementation(libs.navigation.compose)

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)

    // UI
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.kotlinx.datetime)
}