package com.motorro.gradle.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCommonAndroid(ext: CommonExtension) = ext.apply {

    // Take from module name
    namespace = defaultNamespace()

    compileSdk = libs.findVersion("androidCompileSdkVersion").get().requiredVersion.toInt()

    configureKotlin()

    dependencies {
        addDependency("coreLibraryDesugaring", "desugaring")
        implementation("kotlin_coroutines_core")

        testImplementation("test_kotlin")
        testImplementation("test_kotlin_coroutines")
        testImplementation("test_junit")
        testImplementation("test_mockk")
    }
}