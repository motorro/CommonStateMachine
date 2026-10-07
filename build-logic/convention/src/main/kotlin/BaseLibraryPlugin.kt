import com.motorro.gradle.convention.libDesc
import com.motorro.gradle.convention.libId
import com.motorro.gradle.convention.libName
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Base library plugin
 */
class BaseLibraryPlugin : Plugin<Project> {
    override fun apply(targetProject: Project) = with(targetProject) {
        group = rootProject.group
        version = rootProject.version

        println("== Library: **$name**, version: $version ==")
        println("===> Lib id: $libId")
        println("===> Lib name: $libName")
        println("===> Lib desc: ${libDesc ?: "--"}")
    }
}

