import com.motorro.gradle.convention.getProjectProperty
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

val Project.developerId: String?
    get() = (findProperty("developerId") as? String) ?: if (rootProject.extra.has("developerId")) rootProject.extra.get("developerId") as String else null

val Project.developerName: String?
    get() = (findProperty("developerName") as? String) ?: if (rootProject.extra.has("developerName")) rootProject.extra.get("developerName") as String else null

val Project.developerEmail: String?
    get() = (findProperty("developerEmail") as? String) ?: if (rootProject.extra.has("developerEmail")) rootProject.extra.get("developerEmail") as String else null

val Project.projectScm: String?
    get() = (findProperty("projectScm") as? String) ?: if (rootProject.extra.has("projectScm")) rootProject.extra.get("projectScm") as String else null

val Project.projectUrl: String?
    get() = (findProperty("projectUrl") as? String) ?: if (rootProject.extra.has("projectUrl")) rootProject.extra.get("projectUrl") as String else null

val Project.signingKey: String?
    get() = getProjectProperty("signingKey", "SIGNING_KEY")

val Project.signingPassword: String?
    get() = getProjectProperty("signingPassword", "SIGNING_PASSWORD")

val Project.ossrhUsername: String?
    get() = getProjectProperty("ossrhUsername", "OSSRH_USERNAME")

val Project.ossrhPassword: String?
    get() = getProjectProperty("ossrhPassword", "OSSRH_PASSWORD")
