import com.pegio.convention.scriptImplementation
import com.pegio.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "gymbro.android.library")

            dependencies {
                scriptImplementation(project(":core:common"))
                scriptImplementation(project(":core:model"))
                scriptImplementation(project(":feature:common:domain"))

                scriptImplementation(libs.findLibrary("javax-inject").get())
            }
        }
    }
}