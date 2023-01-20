import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
}

group = "cn.rtast"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("nl.vv32.rcon:rcon:1.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    val classValue = "cn.rtast.playerlist.MainKt"
    mainClass.set(classValue)
    mainClassName = classValue
}