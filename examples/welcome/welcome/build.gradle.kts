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
    alias(libs.plugins.android.app)
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
    // Ensure these version variables are defined in gradle.properties or root build.gradle.kts
    compileSdk = androidCompileSdkVersion
    defaultConfig {
        applicationId = "com.motorro.statemachine.welcome"
        minSdk = androidMinSdkVersion
        targetSdk = androidTargetSdkVersion
        versionCode = versionCode
        versionName = versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Changed from minifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
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
    namespace = "com.motorro.statemachine" // Original was com.motorro.statemachine.welcome, but there's also a namespace further down. Ensure this is the correct primary namespace. The one at the end "com.motorro.statemachine" will take precedence.
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":commonstatemachine")) // Simplified project dependency
    implementation(project(":coroutines"))
    implementation(project(":examples:welcome:commonapi"))
    implementation(project(":examples:commoncore"))
    implementation(project(":examples:androidcore"))
    implementation(project(":examples:welcome:login"))
    implementation(project(":examples:welcome:register"))

    coreLibraryDesugaring(libs.desugaring)

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
    implementation(libs.compose.foundation.layouts) // Was foundation.layouts in Groovy

    implementation(libs.hilt.android)
    implementation(libs.hilt.compose) // Verify this maps to 'androidx.hilt:hilt-navigation-compose' or your intended Hilt Compose lib
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.androidx) // Verify this maps to 'com.google.dagger:hilt-android-compiler' or intended Hilt AndroidX compiler

    debugImplementation(libs.compose.tooling)

    testImplementation(libs.bundles.test.core)
    testImplementation(libs.test.androidx.arch)
    testImplementation(libs.test.kotlin.coroutines)
}
