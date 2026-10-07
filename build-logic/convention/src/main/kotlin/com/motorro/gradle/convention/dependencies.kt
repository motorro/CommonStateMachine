package com.motorro.gradle.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

val Project.libs get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

context(project: Project)
fun DependencyHandlerScope.addDependency(configurationName: String, dependencyName: String) {
    project.libs.findLibrary(dependencyName).ifPresent {
        add(configurationName, it)
    }
}

context(project: Project)
fun DependencyHandlerScope.api(dependencyName: String) {
    project.libs.findLibrary(dependencyName).ifPresent {
        add("api", it)
    }
}

context(project: Project)
fun DependencyHandlerScope.implementation(dependencyName: String) {
    project.libs.findLibrary(dependencyName).ifPresent {
        add("implementation", it)
    }
}

context(project: Project)
fun DependencyHandlerScope.testImplementation(dependencyName: String) {
    project.libs.findLibrary(dependencyName).ifPresent {
        add("testImplementation", it)
    }
}