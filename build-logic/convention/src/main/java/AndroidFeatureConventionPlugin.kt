import com.pegio.convention.implementation
import com.pegio.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "gymbro.android.compose")
            apply(plugin = "gymbro.hilt")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
//                implementation(project(":core:designsystem"))

                implementation(libs.findLibrary("androidx-ui").get())
                implementation(libs.findLibrary("androidx-ui-graphics").get())
                implementation(libs.findLibrary("androidx-hilt-navigation-compose").get())
                implementation(libs.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}