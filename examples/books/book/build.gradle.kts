import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/*
 * Copyright 2026 Nikolai Kotchetkov.
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
    alias(libs.plugins.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
}

val androidMinSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra

android {
    compileSdk = androidCompileSdkVersion

    defaultConfig {
        minSdk = androidMinSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        isCoreLibraryDesugaringEnabled = true
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        compose = true
    }
    namespace = "com.motorro.statemachine.books.book"
}

dependencies {
    implementation(project(":commonstatemachine"))
    implementation(project(":coroutines"))
    implementation(project(":commonflow:data"))

    implementation(project(":examples:commoncore"))
    implementation(project(":examples:androidcore"))

    implementation(project(":examples:books:domain"))

    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.kotlin.coroutines.core)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.core)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layouts)
    implementation(libs.compose.material.icons)

    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.androidx)

    debugImplementation(libs.compose.tooling)

    testImplementation(libs.bundles.test.core)
    testImplementation(libs.test.kotlin.coroutines)
}
