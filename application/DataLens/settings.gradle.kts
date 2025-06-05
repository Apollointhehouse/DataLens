pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        id("org.jetbrains.compose") version "1.8.1"
//        id("org.jetbrains.kotlin.plugin.compose") version "1.8.1"
    }
}

rootProject.name = "DataLens"