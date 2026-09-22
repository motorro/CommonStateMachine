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
    alias(libs.plugins.compose)
    alias(libs.plugins.koin)
}

val versionCode: String by project.extra
val versionName: String by project.extra
val androidMinSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra
val androidTargetSdkVersion: Int by project.extra

android {
    compileSdk = androidCompileSdkVersion

    defaultConfig {
        applicationId = "com.motorro.statemachine.skills.app"

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
            isMinifyEnabled = false
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
    namespace = "com.motorro.statemachine.skills.app"
}

dependencies {
    implementation(project(":examples:skills:appcore"))
    implementation(project(":examples:skills:domain"))
    implementation(project(":examples:skills:usecase"))
    implementation(project(":examples:skills:auth:api"))
    implementation(project(":examples:skills:auth:implementation"))
    implementation(project(":commonstatemachine"))
    implementation(project(":coroutines"))
    implementation(project(":commonflow:commonflow-data"))
    implementation(project(":commonflow:commonflow-compose"))
    implementation(project(":commonflow:commonflow-viewmodel"))

    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.napier)
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
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

    debugImplementation(libs.compose.tooling)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.koin.core.viewmodel)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    testImplementation(libs.bundles.test.core)
    testImplementation(libs.test.kotlin.coroutines)
}
