@file:Suppress("unused")

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
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose)
}

val versionName: String by project.extra
val androidMinSdkVersion: Int by project.extra
val androidTargetSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra

group = "com.motorro"
version = versionName

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.motorro.statemachine.timer"
        compileSdk = androidCompileSdkVersion
        minSdk = androidMinSdkVersion

        withHostTest {
            isIncludeAndroidResources = true
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":commonstatemachine"))
            implementation(project(":coroutines"))
            implementation(project(":examples:commoncore"))
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.kotlin.datetime)
        }
        commonTest.dependencies {
            implementation(libs.test.kotlin)
            implementation(libs.test.kotlin.coroutines)
        }
        androidMain.dependencies {
            implementation(libs.timber)
            implementation(libs.kotlin.coroutines.android)

            implementation(project.dependencies.platform(libs.compose.bom))

            implementation(libs.bundles.compose.core)
            implementation(libs.compose.activity)
            implementation(libs.compose.viewmodel)
            implementation(libs.compose.foundation)
            implementation(libs.compose.foundation.layouts)
        }
    }
}

