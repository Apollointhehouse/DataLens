plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.release = 21
}

application {
    // Define the main class for the application.
    mainClass = "me.apollointhehouse.DataLens"
}

tasks.withType<Jar> {
    // Sets main manifest attribute
    manifest {
        attributes["Main-Class"] = "me.apollointhehouse.DataLens"
    }

    archiveBaseName.set("DataLens")

    // Includes dependencies in jar creating a "fat-jar"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
