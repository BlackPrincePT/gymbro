import com.pegio.convention.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class JvmDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "gymbro.jvm.library")

            dependencies {
                implementation(project(":core:common"))
                implementation(project(":feature:common:domain"))
            }
        }
    }
}