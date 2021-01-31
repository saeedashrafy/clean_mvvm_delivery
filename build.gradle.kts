// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion by extra("1.4.21")
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath("com.diffplug.spotless:spotless-plugin-gradle:5.3.0")

    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")

            ktlint(ktlintVersion).userData(
                // TODO this should all come from editorconfig https://github.com/diffplug/spotless/issues/142
                mapOf(
                    "indent_size" to "2",
                    "kotlin_imports_layout" to "ascii"
                )
            )

            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }

        format("xml") {
            target("**/res/**/*.xml")

            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }

        kotlinGradle {
            target("**/*.gradle.kts", "*.gradle.kts")

            ktlint(ktlintVersion).userData(
                mapOf(
                    "indent_size" to "2",
                    "kotlin_imports_layout" to "ascii"
                )
            )

            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
    }
}


allprojects {

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            sourceCompatibility = JavaVersion.VERSION_1_8.toString()
            targetCompatibility = JavaVersion.VERSION_1_8.toString()
        }
    }

    repositories {
        google()
        jcenter()
        maven{ url = uri("https://jitpack.io")}
    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}