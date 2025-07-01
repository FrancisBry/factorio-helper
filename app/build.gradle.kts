plugins {
    kotlin("jvm") version "2.2.0"
    id("application")
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    implementation("org.apache.commons:commons-lang3:3.17.0")

    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "factorio.helper.AppKt"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
