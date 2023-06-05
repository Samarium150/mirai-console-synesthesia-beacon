import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.8.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.14.0"
    id("com.geoffgranum.gradle-conventional-changelog") version "+"
}

group = "io.github.samarium150"
version = "0.1.0"

repositories {
    mavenCentral()
}

tasks {
    withType<KotlinCompile>().all {
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        kotlinOptions.jvmTarget = "11"
    }
    changelog {
        appName = project.name
        versionNum = "$version"
        repoUrl = "https://github.com/Samarium150/mirai-console-synesthesia-beacon"
        trackerUrl = "https://github.com/Samarium150/mirai-console-synesthesia-beacon/issues"
    }
}
