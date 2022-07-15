@file:Suppress("UNUSED_VARIABLE", "DSL_SCOPE_VIOLATION")

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

val versionName: String by project.extra
val androidMinSdkVersion: Int by project.extra
val androidTargetSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra

group = "com.motorro"
version = versionName

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }

    js(IR) {
        compilations.all {
            kotlinOptions.freeCompilerArgs += listOf(
                "-Xopt-in=kotlin.js.ExperimentalJsExport"
            )
        }
        binaries.library()
        useCommonJs()
        browser {
            testTask {
                useMocha {
                    timeout = "10s"
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.coroutines.core)
                implementation(project(":commoncore"))
                implementation(project(":commonapi"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin)
                implementation(libs.test.kotlin.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.timber)
            }
        }
        val androidTest by getting
        val jsMain by getting
        val jsTest by getting
    }
    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.RequiresOptIn"
                )
            }
        }
    }
}

android {
    compileSdk = androidCompileSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = androidMinSdkVersion
        targetSdk = androidTargetSdkVersion
    }
}
