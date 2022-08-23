plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.1"
}

mirai {
    coreVersion = "2.6-RC" // mirai-core version

    publishing {
        repo = "mirai"
        packageName = "eve-bot-plugin"
        override = true
    }
}

dependencies {
    implementation("khttp:khttp:1.0.0")
    implementation("com.google.code.gson:gson:2.8.5")
}

kotlin.sourceSets.all { languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn") }

group = "rocks.ditto"
version = "0.1.4"

repositories {
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
}