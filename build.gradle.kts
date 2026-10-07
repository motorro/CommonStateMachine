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

import com.motorro.gradle.convention.buildVersionCode
import com.motorro.gradle.convention.buildVersionName
import com.motorro.gradle.convention.setSigningConfig
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.lib) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.kotlin.dokka) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.koin) apply false
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.git)
}

version = buildVersionName()
group = "com.motorro.commonstatemachine"
description = "Multiplatform state machine for mobile applications"


allprojects {
    val versionName by extra(buildVersionName())
    val versionCode by extra(buildVersionCode())

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

tasks.register("runLifecycleTests") {
    dependsOn(":lifecycle:allTests")
    description = "Run unit tests for the lifecycle extension layer."
}

tasks.register("runCommonflowTests") {
    dependsOn(":commonflow:commonflow-viewmodel:allTests")
    description = "Run unit tests for the commonflow library."
}

tasks.register("runLceExampleUnitTests") {
    dependsOn(":examples:lce:testDebugUnitTest")
    description = "Run unit tests for LCE app."
}

tasks.register("runWelcomeExampleUnitTests") {
    dependsOn(":examples:welcome:register:allTests")
    dependsOn(":examples:welcome:login:testDebugUnitTest")
    dependsOn(":examples:welcome:app:testDebugUnitTest")
    description = "Run unit tests for welcome app."
}

tasks.register("runTimerExampleUnitTests") {
    dependsOn(":examples:timer:testAndroidHostTest")
    description = "Run unit tests for timer library."
}

tasks.register("runDiExampleUnitTests") {
    dependsOn(":examples:di:login:testDebugUnitTest")
    dependsOn(":examples:di:app:testLoginDebugUnitTest")
    description = "Run unit tests for di app."
}

tasks.register("runBooksExampleUnitTests") {
    dependsOn(":examples:books:book:testDebugUnitTest")
    dependsOn(":examples:books:app:testDebugUnitTest")
    description = "Run unit tests for books app."
}

tasks.register("runSkillsExampleUnitTests") {
    dependsOn(":examples:skills:app:testDebugUnitTest")
    dependsOn(":examples:skills:auth:implementation:allTests")
    description = "Run unit tests for skills."
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
            "runLifecycleTests",
            "runCommonflowTests",
            "runLceExampleUnitTests",
            "runWelcomeExampleUnitTests",
            "runTimerExampleUnitTests",
            "runDiExampleUnitTests",
            "runBooksExampleUnitTests",
            "runSkillsExampleUnitTests"
    )
    group = "verification"
    description = "Run unit tests for all modules."
}

// Signing and publishing
setSigningConfig()

val ossrhUsername: String? by extra
val ossrhPassword: String? by extra

nexusPublishing {
    repositories {
        println("===> ossrhUsername: $ossrhUsername")
        println("===> ossrhPassword: $ossrhPassword")

        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}
