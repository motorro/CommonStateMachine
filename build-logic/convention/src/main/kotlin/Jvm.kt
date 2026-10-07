import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

internal object Jvm {
    const val JDK_VERSION = 21
    val JVM_TARGET = JvmTarget.JVM_21
    val JAVA_VERSION = JavaVersion.VERSION_21
}