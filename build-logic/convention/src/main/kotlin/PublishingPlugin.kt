import com.motorro.gradle.convention.getProjectProperty
import com.motorro.gradle.convention.libDesc
import com.motorro.gradle.convention.libId
import com.motorro.gradle.convention.libName
import com.motorro.gradle.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension

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

class PublishingPlugin : Plugin<Project> {
    override fun apply(targetProject: Project) = with(targetProject) {
        // Apply plugins
        pluginManager.apply(libs.findPlugin("kotlin_dokka").get().get().pluginId)
        pluginManager.apply("maven-publish")
        pluginManager.apply("signing")

        val javadocJar = tasks.register<Jar>("javadocJar") {
            val dokkaGenerate = tasks.named("dokkaGenerate")
            dependsOn(dokkaGenerate)
            group = "documentation"
            archiveClassifier.set("javadoc")
            from(dokkaGenerate)
        }

        extensions.configure<PublishingExtension> {
            publications.withType<MavenPublication> {
                artifact(javadocJar)
                pom {
                    name.set(provider {
                        libName ?: libId ?: name.toString()
                    })
                    description.set(provider {
                        libDesc ?: libName ?: ""
                    })
                    url.set(projectUrl)
                    licenses {
                        license {
                            name.set("Apache-2.0")
                            url.set("https://apache.org/licenses/LICENSE-2.0")
                        }
                    }
                    developers {
                        developer {
                            id.set(developerId)
                            name.set(developerName)
                            email.set(developerEmail)
                        }
                    }
                    scm {
                        connection.set(projectScm)
                        developerConnection.set(projectScm)
                        url.set(projectUrl)
                    }
                }
            }
        }

        extensions.configure<SigningExtension> {
            useInMemoryPgpKeys(signingKey, signingPassword)
            val publishing = extensions.getByType<PublishingExtension>()
            sign(publishing.publications)
        }

        val signingTasks = tasks.withType<Sign>()
        tasks.withType<AbstractPublishToMaven>().configureEach {
            dependsOn(signingTasks)
        }
    }
}
