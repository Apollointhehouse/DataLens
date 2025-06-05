import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.1.20"
    id("org.jetbrains.compose") version "1.8.1"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
}

group = "me.apollointhehouse"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    // Utils
    implementation("info.debatty:java-string-similarity:2.0.0")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("commons-io:commons-io:2.19.0")
    implementation("com.github.vatbub:mslinks:1.0.6.2")

    // UI
    implementation(compose.desktop.currentOs)

    // Logging
    implementation("org.apache.logging.log4j:log4j-api:2.24.3")
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.3")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DataLens"
            packageVersion = "1.0.0"
        }

        buildTypes.release.proguard {
            isEnabled.set(false)
        }
    }
}