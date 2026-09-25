/**
 * Kotlin options
 */
object Kotlin {
    val optIn = listOf(
        "kotlin.RequiresOptIn",
        "kotlinx.coroutines.ExperimentalCoroutinesApi",
        "kotlin.time.ExperimentalTime"
    )
    val compilerArgs = listOf(
        "-Xexpect-actual-classes"
    )
}