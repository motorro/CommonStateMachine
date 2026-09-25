import com.motorro.gradle.convention.configureKotlinJvm
import com.motorro.gradle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with

class JvmPlugin : Plugin<Project> {
    override fun apply(targetProject: Project) {
        // First base library.
        targetProject.pluginManager.apply("motorro.baseLibrary")

        with(targetProject) {
            pluginManager.apply(libs.findPlugin("kotlin_jvm").get().get().pluginId)
            configureKotlinJvm()
        }
    }
}