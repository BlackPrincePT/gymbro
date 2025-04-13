import com.pegio.convention.scriptImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidDataConventionPlugin  : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            apply(plugin = "gymbro.android.library")
            apply(plugin = "gymbro.hilt")

            dependencies{
                scriptImplementation(project(":core:common"))
                scriptImplementation(project(":core:model"))
                scriptImplementation(project(":feature:common:domain"))
                scriptImplementation(project(":feature:common:data"))
            }
        }

    }
}