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
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

val versionCode: String by project.extra
val versionName: String by project.extra
val androidMinSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra
val androidTargetSdkVersion: Int by project.extra

android {
    compileSdk = androidCompileSdkVersion // Assuming androidCompileSdkVersion is defined elsewhere (e.g., gradle.properties or root build.gradle)

    defaultConfig {
        applicationId = "com.motorro.statemachine.lce"
        minSdk = androidMinSdkVersion // Assuming androidMinSdkVersion is defined elsewhere
        targetSdk = androidTargetSdkVersion // Assuming androidTargetSdkVersion is defined elsewhere
        versionCode = versionCode // Assuming versionCode is defined elsewhere
        versionName = versionName // Assuming versionName is defined elsewhere

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.motorro.statemachine.lce"
}

dependencies {
    implementation(project(":commonstatemachine"))
    implementation(project(":coroutines"))
    implementation(project(":examples:commoncore"))
    implementation(project(":examples:androidcore"))

    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.timber)

    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(platform(libs.compose.bom))

    implementation(libs.bundles.compose.core)
    implementation(libs.compose.activity)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layouts)
    debugImplementation(libs.compose.material)
    debugImplementation(libs.compose.material.icons)

    debugImplementation(libs.compose.tooling)

    testImplementation(libs.bundles.test.core)
    testImplementation(libs.test.androidx.arch)
    testImplementation(libs.test.kotlin.coroutines)
}
