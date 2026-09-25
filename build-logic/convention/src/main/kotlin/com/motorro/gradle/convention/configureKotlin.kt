package com.motorro.gradle.convention

import Jvm
import Kotlin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(Jvm.JVM_TARGET)
            compilerOptions.optIn.addAll(Kotlin.optIn)
            compilerOptions.freeCompilerArgs.addAll(Kotlin.compilerArgs)
        }
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = Jvm.JAVA_VERSION
        targetCompatibility = Jvm.JAVA_VERSION
    }

    extensions.configure<KotlinProjectExtension> {
        jvmToolchain(Jvm.JDK_VERSION)
    }

    configureKotlin()
}