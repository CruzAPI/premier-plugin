plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.8"
    id("io.freefair.lombok") version "8.11"
}

group = "net.premierstudios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}