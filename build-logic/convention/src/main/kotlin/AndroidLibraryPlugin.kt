import com.android.build.api.dsl.LibraryExtension
import com.motorro.gradle.convention.configureCommonAndroid
import com.motorro.gradle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(targetProject: Project) = with(targetProject) {
        // First base library.
        targetProject.pluginManager.apply("motorro.baseLibrary")

        // Core plugins
        pluginManager.apply(libs.findPlugin("android_lib").get().get().pluginId)

        extensions.configure<LibraryExtension> {

            configureCommonAndroid(this)

            androidResources.enable = true

            defaultConfig {
                minSdk = libs.findVersion("androidMinSdkVersion").get().requiredVersion.toInt()
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
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
                    all {
                        it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                    }
                }
            }
        }
    }
}
