import com.android.build.api.dsl.ApplicationExtension
import com.motorro.gradle.convention.addDependency
import com.motorro.gradle.convention.configureCommonAndroid
import com.motorro.gradle.convention.implementation
import com.motorro.gradle.convention.libs
import com.motorro.gradle.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(targetProject: Project) = with(targetProject) {
        val vName = rootProject.extra.get("versionName") as String
        val vCode = rootProject.extra.get("versionCode") as Int

        println("== Application: **$name**, version: $vName($vCode) ==")

        // Core plugins
        pluginManager.apply(libs.findPlugin("android_app").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("compose").get().get().pluginId)

        extensions.configure<ApplicationExtension> {

            configureCommonAndroid(this)

            defaultConfig {
                minSdk = libs.findVersion("androidMinSdkVersion").get().requiredVersion.toInt()
                targetSdk = libs.findVersion("androidTargetSdkVersion").get().requiredVersion.toInt()
                versionName = vName
                versionCode = vCode
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    isMinifyEnabled = true
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

        dependencies {
            addDependency("coreLibraryDesugaring", "desugaring")

            implementation("androidx_core")
            implementation("androidx_activity")
            implementation("androidx_lifecycle_runtime")
            implementation("androidx_lifecycle_viewmodel")

            implementation("kotlin_coroutines_core")
            implementation("kotlin_coroutines_android")

            add("implementation", platform(libs.findLibrary("compose_bom").get()))
            implementation("compose_ui")
            implementation("compose_tooling_preview")
            implementation("compose_material")
            implementation("compose_activity")
            implementation("compose_viewmodel")
            implementation("compose_foundation")
            implementation("compose_foundation_layouts")

            implementation("napier")

            addDependency("debugImplementation", "compose_tooling")
            addDependency("debugImplementation", "test_compose_manifest")

            testImplementation("compose_test_junit")
        }
    }
}

