import org.jetbrains.kotlin.gradle.dsl.JvmTarget


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

val exposedVersion: String by project
val jdbcVersion: String by project

val kotlinResultVersion: String by project

val coroutinesVersion: String by project

val log4jVersion: String by project
val kotlinLoggingVersion: String by project

val routingComposeVersion: String by project

dependencies {
    // Utils
    implementation("info.debatty:java-string-similarity:2.0.0")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("commons-io:commons-io:2.19.0")

    // UI
    implementation(compose.desktop.currentOs)
    implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.1")

    // Routing
    implementation("app.softwork:routing-compose:$routingComposeVersion")

    // Logging
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // Exposed for database access (sqlite)
    implementation("org.xerial:sqlite-jdbc:${jdbcVersion}")
    implementation("org.jetbrains.exposed:exposed-core:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-dao:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")

    // Testing Frameworks
    testImplementation(kotlin("test"))

    // Result type
    implementation("com.michael-bull.kotlin-result:kotlin-result:$kotlinResultVersion")
    implementation("com.michael-bull.kotlin-result:kotlin-result-coroutines:$kotlinResultVersion")
}

kotlin {
//    jvmToolchain {
//        languageVersion.set(JavaLanguageVersion.of(21))
//    }

    compilerOptions.jvmTarget = JvmTarget.JVM_11
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release = 11
    }

    jar {
        manifest.attributes["Main-Class"] = "me.apollointhehouse.MainKt"

        val dependencies = configurations
            .runtimeClasspath
            .get()
            .map { zipTree(it) }
        from(dependencies)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        archiveBaseName.set("app")
    }
}

tasks.test {
    useJUnitPlatform()
}

//tasks.register("createDistributable") {
//    dependsOn(tasks.jar)
//
//    doLast {
//        val dir = layout.buildDirectory.dir("libs").get().asFile
//
//        val jarFile = dir.listFiles()?.firstOrNull() ?: error("Failed to get jar")
//        val jreFile = file(dir.path + "/jre").also {
//            if (!it.exists()) error("jre must be provided in libs/jre folder")
//        }
//
//        val out = file(dir.path + "/DataLens").also {
//            if (!it.exists()) it.mkdirs()
//        }
//
//        jarFile.copyTo(out)
//        jreFile.copyTo(out)
//
//
//        file(out.path + "/run.bat").also {
//            it.createNewFile()
//            it.writeText("${jreFile.path}/bin/java.exe -jar ${out.path}/DataLens-1.0.jar")
//        }
//    }
//}