plugins {
    java
}

group = "dev.pyrrha"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:4.4.0_350")
    implementation("ch.qos.logback:logback-classic:1.2.7")
}
