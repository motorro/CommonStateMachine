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

@file:Suppress("UNUSED_VARIABLE", "DSL_SCOPE_VIOLATION")

import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URI

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.dokka")
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

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    android {
        publishLibraryVariants("release", "debug")
    }

    js(IR) {
        compilations.all {
            kotlinOptions.freeCompilerArgs += listOf(
                "-Xopt-in=kotlin.js.ExperimentalJsExport"
            )
        }
        binaries.library()
        useCommonJs()
        browser {
            testTask {
                useMocha {
                    timeout = "10s"
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "coroutines"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":commonstatemachine"))
                implementation(libs.kotlin.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin)
                implementation(libs.test.kotlin.coroutines)
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val androidMain by getting {
            dependsOn(jvmMain)
        }
        val androidTest by getting {
            dependsOn(commonTest)
        }
        val jsMain by getting
        val jsTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.RequiresOptIn"
                )
            }
        }
    }
}

android {
    compileSdk = androidCompileSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = androidMinSdkVersion
        targetSdk = androidTargetSdkVersion
    }
    namespace = "com.motorro.commonstatemachine.coroutines"
}
val dokkaHtml by tasks.getting(DokkaTask::class)

val javadocJar by tasks.creating(Jar::class) {
    dependsOn(dokkaHtml)
    group = "documentation"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

val libId = "coroutines"
val libName = "coroutines"
val libDesc = "Coroutines extension for CommonStateMachine"
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
    repositories {
        maven {
            name = "sonatype"
            url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
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

    signing {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}