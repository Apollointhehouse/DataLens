plugins {
    id("java")
}

group = "me.apollointhehouse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Utils
    implementation("info.debatty:java-string-similarity:2.0.0")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("commons-io:commons-io:2.19.0")
    implementation("com.github.vatbub:mslinks:1.0.6.2")

    // UI
    implementation("com.formdev:flatlaf:3.5.4")
    implementation("com.miglayout:miglayout-swing:11.2")

    // Logging
    implementation("org.apache.logging.log4j:log4j-api:2.24.3")
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.3")


//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release = 21
    }

    jar {
        manifest.attributes["Main-Class"] = "me.apollointhehouse.Main"
        val dependencies = configurations
            .runtimeClasspath
            .get()
            .map { zipTree(it) }
        from(dependencies)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}