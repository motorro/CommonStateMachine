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
@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.dokka)
    id("maven-publish")
    id("signing")
}

val versionName: String by project.extra
val androidMinSdkVersion: Int by project.extra
val androidTargetSdkVersion: Int by project.extra
val androidCompileSdkVersion: Int by project.extra

group = rootProject.group
version = rootProject.version

println("== Project version: $versionName ==")

kotlin {
    jvmToolchain(17)

    jvm()
    android {
        namespace = "com.motorro.commonstatemachine.commonflow.data"
        compileSdk = androidCompileSdkVersion
        minSdk = androidMinSdkVersion

        withHostTest {
            isIncludeAndroidResources = true
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    js(IR) {
        binaries.library()
        useCommonJs()
        browser {
            testTask(Action {
                useMocha {
                    timeout = "10s"
                }
            })
        }
    }

    wasmJs {
        binaries.library()
        useCommonJs()
        browser {
            testTask(Action {
                useMocha {
                    timeout = "10s"
                }
            })
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "commonflow-data"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":commonstatemachine"))
        }
    }
}
val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaGenerate)
    group = "documentation"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaGenerate)
}

val libId = "commonflow-data"
val libName = "commonflow-data"
val libDesc = "Common data flow for modularized state machines"
val projectUrl: String by project.extra
val projectScm: String by project.extra
val ossrhUsername: String? by rootProject.extra
val ossrhPassword: String? by rootProject.extra
val developerId: String by project.extra
val developerName: String by project.extra
val developerEmail: String by project.extra
val signingKey: String? by rootProject.extra
val signingPassword: String? by rootProject.extra

publishing {
    publications.withType<MavenPublication> {
        artifact(javadocJar)
        pom {
            name.set(libName)
            description.set(libDesc)
            url.set(projectUrl)
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://apache.org/licenses/LICENSE-2.0")
                }
            }
            developers {
                developer {
                    id.set(developerId)
                    name.set(developerName)
                    email.set(developerEmail)
                }
            }
            scm {
                connection.set(projectScm)
                developerConnection.set(projectScm)
                url.set(projectUrl)
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}