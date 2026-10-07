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
    alias(libs.plugins.motorro.android.app)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
}

android {
    flavorDimensions += "auth"
    productFlavors {
        create("login") {
            dimension = "auth"
            applicationIdSuffix = ".login"
        }
        create("social") {
            dimension = "auth"
            applicationIdSuffix = ".social"
        }
    }
}

dependencies {
    implementation(project(":commonstatemachine"))
    implementation(project(":coroutines"))
    implementation(project(":examples:commoncore"))
    implementation(project(":examples:di:api"))

    "loginImplementation"(project(":examples:di:login"))
    "socialImplementation"(project(":examples:di:social"))

    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.androidx)

    debugImplementation(libs.compose.material.icons)
}
