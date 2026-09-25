plugins {
    `kotlin-dsl`
}

group = "com.motorro.gradle.convention"

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.compose.multiplatform.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-Xinline-classes",
            "-Xcontext-parameters"
        ))
    }
}

gradlePlugin {
    plugins {
        register("baseLibrary"){
            id = "motorro.baseLibrary"
            implementationClass = "BaseLibraryPlugin"
        }
        register("jvmLibrary"){
            id = "motorro.jvmLibrary"
            implementationClass = "JvmLibraryPlugin"
        }
        register("androidLibrary"){
            id = "motorro.androidLibrary"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidComposeLibrary"){
            id = "motorro.androidComposeLibrary"
            implementationClass = "AndroidComposeLibraryPlugin"
        }
        register("androidApplication"){
            id = "motorro.androidApplication"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidTest"){
            id = "motorro.androidTest"
            implementationClass = "AndroidTestPlugin"
        }
        register("androidDynamicFreature"){
            id = "motorro.androidDynamicFeature"
            implementationClass = "AndroidDynamicFeaturePlugin"
        }
        register("kotlinMultiplatform"){
            id = "motorro.kmp"
            implementationClass = "KmpPlugin"
        }
        register("composeMultiplatform"){
            id = "motorro.cmp"
            implementationClass = "CmpPlugin"
        }
        register("publishing"){
            id = "motorro.publishing"
            implementationClass = "PublishingPlugin"
        }
    }
}