import com.android.build.gradle.LibraryExtension
import com.pegio.convention.scriptAndroidTestImplementation
import com.pegio.convention.scriptDebugImplementation
import com.pegio.convention.scriptImplementation
import com.pegio.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "gymbro.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                val bom = libs.findLibrary("androidx-compose-bom").get()
                scriptImplementation(platform(bom))
                scriptAndroidTestImplementation(platform(bom))
                scriptImplementation(libs.findLibrary("androidx-ui-tooling-preview").get())
                scriptDebugImplementation(libs.findLibrary("androidx-ui-tooling").get())
            }
        }
    }
}