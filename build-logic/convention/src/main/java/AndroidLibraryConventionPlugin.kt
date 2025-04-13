import com.android.build.gradle.LibraryExtension
import com.pegio.convention.scriptAndroidTestImplementation
import com.pegio.convention.configureKotlinAndroid
import com.pegio.convention.libs
import com.pegio.convention.scriptTestImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig.targetSdk = 35
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                resourcePrefix =
                    path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
                        .lowercase() + "_"
            }

            dependencies {
                scriptTestImplementation(libs.findLibrary("junit").get())
                scriptAndroidTestImplementation(libs.findLibrary("androidx-junit").get())
                scriptAndroidTestImplementation(libs.findLibrary("androidx-espresso-core").get())
            }
        }
    }
}