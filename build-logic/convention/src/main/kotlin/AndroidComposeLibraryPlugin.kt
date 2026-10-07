import com.android.build.api.dsl.LibraryExtension
import com.motorro.gradle.convention.addDependency
import com.motorro.gradle.convention.implementation
import com.motorro.gradle.convention.libs
import com.motorro.gradle.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeLibraryPlugin : Plugin<Project> {

    override fun apply(targetProject: Project) = with(targetProject) {
        // Core plugin
        pluginManager.apply("motorro.androidLibrary")
        pluginManager.apply(libs.findPlugin("compose").get().get().pluginId)

        extensions.configure<LibraryExtension> {
            buildFeatures {
                compose = true
            }
        }

        dependencies {
            add("implementation", platform(libs.findLibrary("compose_bom").get()))

            implementation("compose_runtime")
            implementation("compose_ui")
            implementation("compose_tooling_preview")
            implementation("compose_foundation")
            implementation("compose_foundation_layouts")
            implementation("compose_material")

            addDependency("debugImplementation", "compose_tooling")
            addDependency("debugImplementation", "test_compose_manifest")

            testImplementation("compose_test_junit")
        }
    }
}
