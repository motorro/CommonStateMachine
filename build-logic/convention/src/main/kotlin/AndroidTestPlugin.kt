import com.android.build.api.dsl.TestExtension
import com.motorro.gradle.convention.configureKotlin
import com.motorro.gradle.convention.defaultNamespace
import com.motorro.gradle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(targetProject: Project) = with(targetProject) {
        // Core plugins
        pluginManager.apply(libs.findPlugin("android_test").get().get().pluginId)

        extensions.configure<TestExtension> {
            // Take from module name
            namespace = defaultNamespace()

            compileSdk = libs.findVersion("androidCompileSdkVersion").get().requiredVersion.toInt()

            configureKotlin()

            defaultConfig {
                minSdk = libs.findVersion("androidMinTestSdk").get().requiredVersion.toInt()
                targetSdk = libs.findVersion("androidTargetSdkVersion").get().requiredVersion.toInt()
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
            }

            compileOptions {
                sourceCompatibility = Jvm.JAVA_VERSION
                targetCompatibility = Jvm.JAVA_VERSION
            }
        }
    }
}
