@file:Suppress("unused")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/*
 * Copyright 2022 Nikolai Kotchetkov.
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

plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose)
}

val versionCode: String by project.extra
val versionName: String by project.extra
val androidMinSdkVersion: Int by project.extra
val androidTargetSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra

android {
    // Assuming androidCompileSdkVersion, androidMinSdkVersion, and androidTargetSdkVersion
    // are defined in your project's gradle.properties or root build.gradle.kts
    compileSdk = androidCompileSdkVersion

    defaultConfig {
        minSdk = androidMinSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
    }
    namespace = "com.motorro.statemachine.login"
}

dependencies {
    implementation(project(":commonstatemachine"))
    implementation(project(":coroutines"))
    implementation(project(":examples:welcome:commonapi"))
    implementation(project(":examples:commoncore"))
    implementation(project(":examples:androidcore"))

    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(platform(libs.compose.bom))

    implementation(libs.bundles.compose.core)
    implementation(libs.compose.activity)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layouts) // Corrected from "foundation.layout" to "foundation.layouts" as per your original file

    implementation(libs.hilt.android)
    implementation(libs.hilt.compose) // Assuming this is 'libs.hilt.navigation.compose' or similar based on common usage with Hilt and Compose
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.androidx) // Assuming this is 'libs.hilt.android.compiler' or similar

    debugImplementation(libs.compose.tooling)

    testImplementation(libs.bundles.test.core)
    testImplementation(libs.test.androidx.arch)
    testImplementation(libs.test.kotlin.coroutines)
}
