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
    alias(libs.plugins.koin)
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
        namespace = "com.motorro.statemachine.skills.usecase"
        compileSdk = androidCompileSdkVersion
        minSdk = androidMinSdkVersion

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":examples:skills:domain"))
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.napier)
        }
    }
}

