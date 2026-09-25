import com.android.build.api.dsl.DynamicFeatureExtension
import com.motorro.gradle.convention.configureCommonAndroid
import com.motorro.gradle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidDynamicFeaturePlugin : Plugin<Project> {

    override fun apply(targetProject: Project) = with(targetProject) {
        // Core plugins
        pluginManager.apply(libs.findPlugin("android_dynamic_feature").get().get().pluginId)

        extensions.configure<DynamicFeatureExtension> {

            configureCommonAndroid(this)

            defaultConfig {
                minSdk = libs.findVersion("androidMinSdkVersion").get().requiredVersion.toInt()
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }

            compileOptions {
                sourceCompatibility = Jvm.JAVA_VERSION
                targetCompatibility = Jvm.JAVA_VERSION

                isCoreLibraryDesugaringEnabled = true
            }

            testOptions {
                unitTests {
                    isIncludeAndroidResources = true
                }
            }
        }
    }
}
