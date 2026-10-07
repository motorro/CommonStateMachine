import com.motorro.gradle.convention.defaultNamespace
import com.motorro.gradle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class CmpPlugin : Plugin<Project> {

    override fun apply(targetProject: Project) = with(targetProject) {
        // First base KMP convention.
        pluginManager.apply("motorro.kmp")

        // Then Compose side.
        pluginManager.apply(libs.findPlugin("compose").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("compose_multiplatform").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("compose_hot_reload").get().get().pluginId)

        extensions.configure<KotlinMultiplatformExtension> {

            sourceSets.apply {
                commonMain.dependencies {
                    implementation(libs.findLibrary("compose_multiplatform_runtime").get())
                    implementation(libs.findLibrary("compose_multiplatform_ui").get())
                    implementation(libs.findLibrary("compose_multiplatform_foundation").get())
                    implementation(libs.findLibrary("compose_multiplatform_material3").get())

                    // KMP resources (Res.drawable.*)
                    implementation(libs.findLibrary("compose_multiplatform_resources").get())

                    // Enable KMP @Preview for @Compose functions.
                    implementation(libs.findLibrary("compose_multiplatform_preview").get())
                }
            }
        }

        dependencies {
            add("androidRuntimeClasspath", libs.findLibrary("compose_multiplatform_tooling").get())
        }

        extensions.configure<ComposeExtension> {
            configure<ResourcesExtension> {
                publicResClass = false
                packageOfResClass = defaultNamespace()
                generateResClass = always
            }
        }
    }
}