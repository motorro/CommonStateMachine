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

dependencies {
    implementation(project(":commonstatemachine"))
    implementation(project(":commonflow:commonflow-data"))
    implementation(project(":commonflow:commonflow-compose"))
    implementation(project(":commonflow:commonflow-viewmodel"))

    implementation(project(":examples:commoncore"))
    implementation(project(":examples:books:domain"))
    implementation(project(":examples:books:book"))

    implementation(libs.kotlin.immutable)

    implementation(libs.compose.material.icons)

    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.androidx)
}
