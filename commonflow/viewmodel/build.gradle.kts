import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

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
    alias(libs.plugins.motorro.cmp)
    alias(libs.plugins.motorro.publishing)
}

kotlin {
    @OptIn(ExperimentalAbiValidation::class)
    abiValidation {

    }

    sourceSets {
        commonMain.dependencies {
            api(project(":commonstatemachine"))
            api(project(":coroutines"))
            api(project(":commonflow:commonflow-compose"))
            api(libs.compose.multiplatform.viewmodel)
            implementation(libs.compose.multiplatform.lifecycle)
        }
        commonTest.dependencies {
            implementation(libs.test.kotlin)
            implementation(libs.test.kotlin.coroutines)
        }
        androidMain.dependencies {
            api(libs.androidx.appcompat)
            implementation(libs.androidx.activity)
            implementation(libs.androidx.fragment)
            implementation(libs.compose.activity)
        }
    }
}
