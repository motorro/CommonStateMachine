package com.motorro.gradle.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.androidLibrary(block: KotlinMultiplatformAndroidLibraryTarget.() -> Unit) {
    configure<KotlinMultiplatformAndroidLibraryTarget>(block)
}