import com.pegio.convention.scriptImplementation
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
                scriptImplementation(project(":core:common"))
                scriptImplementation(project(":core:model"))
                scriptImplementation(project(":core:designsystem"))
                scriptImplementation(project(":core:domain"))
                scriptImplementation(project(":feature:common"))

                scriptImplementation(libs.findLibrary("androidx-ui").get())
                scriptImplementation(libs.findLibrary("androidx-ui-graphics").get())
                scriptImplementation(libs.findLibrary("androidx-hilt-navigation-compose").get())
                scriptImplementation(libs.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}