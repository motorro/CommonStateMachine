package com.motorro.gradle.convention

import org.gradle.api.Project

/**
 * Library ID
 */
val Project.libId: String get() = findProperty("libId") as? String ?: frameworkBaseName()

/**
 * Library Name
 */
val Project.libName: String get() = findProperty("libName") as? String ?: libId

/**
 * Library Description
 */
val Project.libDesc: String? get() = findProperty("libDesc") as? String
