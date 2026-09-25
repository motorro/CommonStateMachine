import com.motorro.gradle.convention.androidLibrary
import com.motorro.gradle.convention.defaultNamespace
import com.motorro.gradle.convention.frameworkBaseName
import com.motorro.gradle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpPlugin : Plugin<Project> {
    override fun apply(targetProject: Project) = with(targetProject) {
        // First base library.
        targetProject.pluginManager.apply("motorro.baseLibrary")

        // Core KMP plugins
        pluginManager.apply(libs.findPlugin("kotlin_multiplatform").get().get().pluginId)
        pluginManager.apply(libs.findPlugin("android_kotlin_multiplatform_library").get().get().pluginId)

        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(Jvm.JDK_VERSION)

            compilerOptions {
                jvm {
                    compilerOptions.jvmTarget.set(Jvm.JVM_TARGET)
                }
                compilerOptions.optIn.addAll(Kotlin.optIn)
                compilerOptions.freeCompilerArgs.addAll(Kotlin.compilerArgs)
            }

            androidLibrary {
                namespace = defaultNamespace()
                compileSdk = libs.findVersion("androidCompileSdkVersion").get().requiredVersion.toInt()
                minSdk = libs.findVersion("androidMinSdkVersion").get().requiredVersion.toInt()
                androidResources.enable = true
                compilerOptions.jvmTarget.set(Jvm.JVM_TARGET)

                withHostTest {
                    isIncludeAndroidResources = true
                }

                @Suppress("UnstableApiUsage")
                optimization {
                    consumerKeepRules.apply {
                        publish = true
                        file("consumer-rules.pro")
                    }
                }
            }

            // iOS targets
            listOf(
                iosArm64(),
                iosSimulatorArm64()
            ).forEach {
                it.binaries.framework {
                    baseName = project.frameworkBaseName()
                    isStatic = true
                }
            }

            js {
                outputModuleName.set(project.frameworkBaseName())
                browser()
                binaries.executable()
                binaries.library()
                generateTypeScriptDefinitions()
                compilerOptions {
                    target.set("es2015")
                }
            }

            @OptIn(ExperimentalWasmDsl::class)
            wasmJs {
                browser()
                binaries.executable()
                binaries.library()
                useCommonJs()
            }

            sourceSets.apply {
                commonMain {
                    dependencies {
                        implementation(libs.findLibrary("kotlin_annotations").get())
                    }
                }
                jvmMain.dependencies {

                }
                commonTest {
                    dependencies {
                        implementation(libs.findLibrary("test_kotlin").get())
                    }
                }
            }
        }
    }
}
