package com.motorro.gradle.convention

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.util.Properties
import kotlin.reflect.KClass

/**
 * Gets extension property
 */
context(project: Project)
inline fun <reified E: Any, T: Any> KClass<E>.extensionProperty(block: E.() -> Property<T>): T {
    return project.extensions.getByType<E>().block().get()
}

/**
 * Takes extra property
 */
inline fun <reified T: Any> Project.extraProperty(name: String): T? = if (extra.has("versionCode")) extra.get("versionCode") as T else null

/**
 * Extra or root project extra property
 */
inline fun <reified T: Any> Project.findExtraProperty(name: String): T? = extraProperty(name) ?: rootProject.extraProperty(name)

private fun Project.loadLocalProperties(): Properties {
    val props = Properties()
    val localPropsFile = File(rootProject.rootDir, "local.properties")
    if (localPropsFile.exists()) {
        localPropsFile.inputStream().use { props.load(it) }
        val signingConfigPath = props.getProperty("SIGNING_CONFIG")
        if (!signingConfigPath.isNullOrBlank()) {
            val signingConfigFile = File(signingConfigPath)
            if (signingConfigFile.exists()) {
                signingConfigFile.inputStream().use { props.load(it) }
            }
        }
    }
    return props
}

fun Project.getProjectProperty(propertyName: String, envName: String): String? {
    val fromProperty = findProperty(propertyName) as? String
    if (!fromProperty.isNullOrBlank()) return fromProperty

    val localProps = loadLocalProperties()
    val fromLocalProps = localProps.getProperty(propertyName)
    if (!fromLocalProps.isNullOrBlank()) return fromLocalProps

    val fromEnv = System.getenv(envName)
    if (!fromEnv.isNullOrBlank()) return fromEnv

    return null
}
