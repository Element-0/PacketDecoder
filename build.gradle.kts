plugins {
  kotlin("jvm") version "1.3.72"
  application
}

group = "one.codehz.elementzero"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()

  maven("https://repo.nukkitx.com/maven-releases/")
  maven("https://repo.nukkitx.com/maven-snapshots/")
  maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("com.nukkitx.protocol:bedrock-v408:2.6.0-SNAPSHOT")
  implementation("com.github.ajalt:clikt:2.6.0")
  implementation("org.xerial:sqlite-jdbc:3.30.1")
}

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = "13"
  }
}

application {
  mainClassName = "one.codehz.elementzero.packetdecoder.AppKt"
}