import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    application
}

repositories {
    maven { url = uri("https://jitpack.io") }
}

applyCommonConfiguration()

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application.mainClass.set("com.sk89q.worldedit.internal.util.DocumentationPrinter")
tasks.named<JavaExec>("run") {
    workingDir = rootProject.projectDir
}

dependencies {
    "implementation"(project(":worldedit-libs:core:ap"))
    "implementation"(project(":worldedit-core"))
    "implementation"(kotlin("stdlib-jdk8"))
    "implementation"(kotlin("reflect"))
    "implementation"("com.google.guava:guava")
}
