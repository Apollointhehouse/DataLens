plugins {
    id("java")
}

group = "me.apollointhehouse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.vincenzopalazzo:material-ui-swing:1.1.2")
    implementation("io.github.material-ui-swing:DarkStackOverflowTheme:0.0.1-rc3")
    implementation("org.jetbrains:annotations:24.0.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}