import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "com.tanishranjan"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.g0dkar:qrcode-kotlin-jvm:3.2.0")
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}