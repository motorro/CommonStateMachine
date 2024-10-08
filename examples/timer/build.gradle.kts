/*
 * Copyright 2023 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
}

val versionName: String by project.extra
val androidMinSdkVersion: Int by project.extra
val androidTargetSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra

group = "com.motorro"
version = versionName

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":commonstatemachine"))
                implementation(project(":coroutines"))
                implementation(project(":examples:commoncore"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.datetime)
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
                implementation(libs.kotlin.coroutines.android)
            }
        }
        val androidUnitTest by getting
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

composeCompiler {
    enableStrongSkippingMode.set(true)

    reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
    stabilityConfigurationFile.set(rootProject.layout.projectDirectory.file("stability_config.conf"))
}

android {
    compileSdk = androidCompileSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = androidMinSdkVersion
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    namespace = "com.motorro.statemachine.timer"

    buildFeatures {
        compose = true
    }

    dependencies {
        coreLibraryDesugaring(libs.desugaring)

        val composeBom = platform(libs.compose.bom)
        implementation(composeBom)
        androidTestImplementation(composeBom)

        implementation(libs.bundles.compose.core)
        implementation(libs.compose.activity)
        implementation(libs.compose.viewmodel)
        implementation(libs.compose.foundation)
        implementation(libs.compose.foundation.layouts)

        debugImplementation(libs.compose.tooling)
    }
}
