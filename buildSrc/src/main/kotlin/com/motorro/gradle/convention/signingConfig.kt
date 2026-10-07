package com.motorro.gradle.convention

import org.gradle.api.Project
import org.gradle.internal.extensions.core.extra
import java.io.FileInputStream
import java.util.Properties

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

// Based on this article: https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/

private fun Project.getSigningFromEnv() {
    extra["signingKey"] = System.getenv("SIGNING_KEY")
    extra["signingPassword"] = System.getenv("SIGNING_PASSWORD")
    extra["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    extra["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    extra["sonatypeStagingProfileId"] = System.getenv("SONATYPE_STAGING_PROFILE_ID")
}

fun Project.setSigningConfig() {
    val secretPropsFile = this.rootProject.file("local.properties")
    if (secretPropsFile.exists()) {
        val lp = Properties()
        FileInputStream(secretPropsFile).use { inputStream ->
            lp.load(inputStream)
        }
        if (lp.containsKey("SIGNING_CONFIG")) {
            val signingConfigFile = file(lp.getProperty("SIGNING_CONFIG"))
            if (signingConfigFile.exists()) {
                val sc = Properties()
                FileInputStream(signingConfigFile).use { inputStream ->
                    sc.load(inputStream)
                }
                sc.forEach { name, value ->
                    if (name != null && value != null) {
                        extra[name.toString()] = value
                    }
                }
            } else {
                getSigningFromEnv()
            }
        } else {
            getSigningFromEnv()
        }
    } else {
        getSigningFromEnv()
    }
}
