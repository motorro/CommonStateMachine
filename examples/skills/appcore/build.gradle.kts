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
    alias(libs.plugins.composeMultiplatform)
}

val versionName: String by project.extra
val androidMinSdkVersion: Int by project.extra
val androidTargetSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra

group = "com.motorro"
version = versionName

kotlin {
    jvmToolchain(21)

    compilerOptions.freeCompilerArgs.addAll(listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xexpect-actual-classes",
        "-Xexplicit-backing-fields",
        "-Xcontext-parameters"
    ))

    android {
        namespace = "com.motorro.statemachine.skills.appcore"
        compileSdk = androidCompileSdkVersion
        minSdk = androidMinSdkVersion

        withHostTest {
            isIncludeAndroidResources = true
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.composeMultiplatform.runtime)
            api(libs.composeMultiplatform.foundation)
            api(libs.composeMultiplatform.ui)
            api(libs.composeMultiplatform.resources)
            api(libs.composeMultiplatform.material3)
            api(libs.composeMultiplatform.icons)
            implementation(libs.composeMultiplatform.navigationevent)
            implementation(libs.composeMultiplatform.preview)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.composeMultiplatform.tooling)
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.motorro.statemachine.skills.appcore"
    generateResClass = always
}


