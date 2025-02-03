plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.11"
}

group = "net.premierstudios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }

    implementation("org.mongodb:mongodb-driver-sync:5.3.0")
}

tasks.shadowJar {
    archiveClassifier = null
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
