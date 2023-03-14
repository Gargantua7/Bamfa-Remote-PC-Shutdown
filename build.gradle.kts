plugins {
    kotlin("jvm") version "1.8.0"
}

group = "com.gargantua7.bemfa"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/nexus/content/repository/central")
    maven("https://maven.aliyun.com/nexus/content/repository/google")
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-network:2.2.4")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
}