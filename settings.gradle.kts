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

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
    versionCatalogs {
        // declares an additional catalog
        create("androidx") { // Changed from "androidx {" to "create("androidx") {"
            from(files("gradle/libs.versions.toml"))
        }
    }
}
rootProject.name = "CommonStateMachine"
include(
    ":tmap",
        ":commonstatemachine",
        ":coroutines",
        ":commonflow:data",
        ":commonflow:compose",
        ":commonflow:viewmodel",
        ":examples:commoncore",
        ":examples:androidcore",
        ":examples:welcome:welcome",
        ":examples:welcome:login",
        ":examples:welcome:commonregister",
        ":examples:welcome:commonapi",
        ":examples:welcome:register",
        ":examples:lce",
        ":examples:timer",
        ":examples:multi:mixed",
        ":examples:multi:navbar",
        ":examples:multi:parallel",
        ":examples:lifecycle",
        ":examples:di:api",
        ":examples:di:login",
        ":examples:di:social",
        ":examples:di:app",
        ":examples:books:domain",
        ":examples:books:book",
        ":examples:books:app"
)
