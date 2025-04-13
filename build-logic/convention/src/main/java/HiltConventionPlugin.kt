import com.pegio.convention.scriptImplementation
import com.pegio.convention.scriptKsp
import com.pegio.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "com.google.dagger.hilt.android")

            dependencies {
                scriptImplementation(libs.findLibrary("hilt-android").get())
                scriptKsp(libs.findLibrary("hilt-android-compiler").get())
            }
        }
    }
}