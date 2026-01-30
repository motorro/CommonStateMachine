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

@file:Suppress("unused")

import groovy.lang.Closure
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.lib) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.dokka) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.git)
}

apply {
    from("$rootDir/gradle/versioning.gradle")
    from("$rootDir/gradle/maven-publish-config.gradle")
}

private val buildVersionName: Closure<Any> by ext
private val buildVersionCode: Closure<Any> by ext

version = buildVersionName()
group = "com.motorro.commonstatemachine"
description = "Multiplatform state machine for mobile applications"


allprojects {
    val versionName by extra(buildVersionName())
    val versionCode by extra(buildVersionCode())

    val androidBuildToolsVersion by extra("35.0.0")
    val androidMinSdkVersion by extra(24)
    val androidTargetSdkVersion by extra(36)
    val androidCompileSdkVersion by extra(36)

    val developerId by extra("motorro")
    val developerName by extra("Nikolai Kotchetkov")
    val developerEmail by extra("motorro@gmail.com")

    val projectScm by extra("https://github.com/motorro/CommonStateMachine.git")
    val projectUrl by extra("https://github.com/motorro/CommonStateMachine")

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-Xexpect-actual-classes"
            ))
        }
    }

    tasks.withType<Test>().configureEach {
        forkEvery = 100
        testLogging {
            events("skipped", "failed")
            showExceptions = true
            exceptionFormat = TestExceptionFormat.FULL
            showCauses = true
            showStackTraces = true

            // set options for log level DEBUG and INFO
            debug {
                events("started", "passed", "skipped", "failed", "standardOut", "standardError")
                exceptionFormat = TestExceptionFormat.FULL
            }
            info.events = debug.events
            info.exceptionFormat = debug.exceptionFormat
        }
    }
}

tasks.register("runStateMachineTests") {
    dependsOn(":commonstatemachine:allTests")
    description = "Run unit tests for the common state machine layer."
}

tasks.register("runCoroutinesTests") {
    dependsOn(":coroutines:allTests")
    description = "Run unit tests for the coroutines extension layer."
}

tasks.register("runCommonflowTests") {
    dependsOn(":commonflow:viewmodel:allTests")
    description = "Run unit tests for the commonflow library."
}

tasks.register("runRegisterUnitTests") {
    dependsOn(":examples:welcome:commonregister:allTests")
    description = "Run unit tests for the common register layer."
}

tasks.register("runLoginUnitTests") {
    dependsOn(":examples:welcome:login:testDebugUnitTest")
    description = "Run unit tests for the login module."
}

tasks.register("runLceUnitTests") {
    dependsOn(":examples:lce:testDebugUnitTest")
    description = "Run unit tests for LCE app."
}

tasks.register("runWelcomeUnitTests") {
    dependsOn(":examples:welcome:welcome:testDebugUnitTest")
    description = "Run unit tests for welcome app."
}

tasks.register("runTimerUnitTests") {
    dependsOn(":examples:timer:testAndroidHostTest")
    description = "Run unit tests for timer library."
}

tasks.register("runDiUnitTests") {
    dependsOn(":examples:di:login:testDebugUnitTest")
    description = "Run unit tests for di app."
}

tasks.register("displayVersion") {
    description = "Display application version name"
    doLast {
        println("Application version: ${buildVersionName()}")
    }
}

tasks.register("runUnitTests") {
    dependsOn(
            "runStateMachineTests",
            "runCoroutinesTests",
            "runCommonflowTests",
            "runLoginUnitTests",
            "runRegisterUnitTests",
            "runLceUnitTests",
            "runWelcomeUnitTests",
            "runTimerUnitTests",
            "runDiUnitTests"
    )
    group = "verification"
    description = "Run unit tests for all modules."
}

val ossrhUsername: String? by extra
val ossrhPassword: String? by extra

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}
