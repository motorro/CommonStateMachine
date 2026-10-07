package com.motorro.gradle.convention

import org.gradle.api.GradleException
import org.gradle.api.Project

private fun getGitDescribe(project: Project): String? {
    val grgit = project.findProperty("grgit") ?: project.rootProject.findProperty("grgit")
    if (grgit != null) {
        try {
            val describeMethod = grgit.javaClass.getMethod("describe", Map::class.java)
            val result = describeMethod.invoke(grgit, mapOf("match" to listOf("v[0-9]*"))) as? String
            if (!result.isNullOrBlank()) return result
        } catch (e: Exception) {
            project.logger.info("Failed to invoke grgit.describe: {}", e.message)
        }
    }
    return try {
        val process = ProcessBuilder("git", "describe", "--match", "v[0-9]*")
            .directory(project.rootDir)
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .start()
        val output = process.inputStream.bufferedReader().readText().trim()
        if (process.waitFor() == 0 && output.isNotEmpty()) output else null
    } catch (e: Exception) {
        null
    }
}

private fun getGitBranchName(project: Project): String? {
    val grgit = project.findProperty("grgit") ?: project.rootProject.findProperty("grgit")
    if (grgit != null) {
        try {
            val branchProp = grgit.javaClass.getMethod("getBranch").invoke(grgit)
            val currentMethod = branchProp?.javaClass?.getMethod("current")
            val currentBranch = currentMethod?.invoke(branchProp)
            val nameMethod = currentBranch?.javaClass?.getMethod("getName")
            val name = nameMethod?.invoke(currentBranch) as? String
            if (!name.isNullOrBlank()) return name
        } catch (e: Exception) {
            project.logger.info("Failed to invoke grgit.branch: {}", e.message)
        }
    }
    return try {
        val process = ProcessBuilder("git", "rev-parse", "--abbrev-ref", "HEAD")
            .directory(project.rootDir)
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .start()
        val output = process.inputStream.bufferedReader().readText().trim()
        if (process.waitFor() == 0 && output.isNotEmpty() && output != "HEAD") output else null
    } catch (e: Exception) {
        null
    }
}

/**
 * Builds app version using latest repository tag
 * Tag should be a semantic version of major/minor at least
 * Function takes number of commits since last tag and uses it to calculate patch number
 * - If patch is omitted in tag - uses number of commits
 * - If patch version set in tag - pluses number of commits to patch number
 * @return Semantic version string
 */
fun Project.buildVersionName(): String {
    val versionRegexp = Regex("""^v?(\d+)\.(\d+)\.?(\d*)-?(\d*)""")

    val fromEnv = (findProperty("versionName") as? String) ?: System.getenv("VERSION_NAME")
    if (!fromEnv.isNullOrBlank() && versionRegexp.containsMatchIn(fromEnv)) {
        logger.info("Version from ENV: {}", fromEnv)
        return fromEnv
    }

    val described = getGitDescribe(this)
    val matchResult = if (described != null) versionRegexp.find(described) else null

    val (major, minor, patch, changes) = if (matchResult == null) {
        logger.info("Git description does not have any version info. Make sure you have a semver tag (v1.2) or (v1.2.3) in a repository.")
        logger.info("Using `v0.0.1` for this build")
        listOf(0, 0, 1, 0)
    } else {
        val groups = matchResult.groupValues
        val maj = groups[1].toIntOrNull() ?: 0
        val min = groups[2].toIntOrNull() ?: 0
        val pat = groups[3].takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0
        val cha = groups[4].takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0
        listOf(maj, min, pat, cha)
    }

    val fromGit = "$major.$minor.${patch + changes}"
    logger.info("Version evaluated using Git: {}", fromGit)
    return fromGit
}

/**
 * Builds version code using [buildVersionName]
 * @return Integer version code
 */
fun Project.buildVersionCode(versionName: String = buildVersionName()): Int {
    /*
        Max version code (INT32) is 214 74 836 47
        Spaces denote positions of:
        - major
        - minor
        - build
        - build variant
     */
    val parts = versionName.split('.').mapNotNull { it.toIntOrNull() }
    val major = parts.getOrElse(0) { 0 }
    val minor = parts.getOrElse(1) { 0 }
    val patch = parts.getOrElse(2) { 0 }

    if (major > 214) {
        throw GradleException("Major version exhausted: version greater than 214 not supported")
    }
    if (minor > 99) {
        throw GradleException("Minor version exhausted: version greater than 99 not supported")
    }
    if (patch > 999) {
        throw GradleException("Patch version exhausted: version greater than 999 not supported")
    }

    val version = major * 10000000 + minor * 100000 + patch * 100
    logger.info("Version code evaluated using Git: {}", version)
    return version
}
