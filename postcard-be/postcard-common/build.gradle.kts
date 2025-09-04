plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.kotlinx.datetime)

    implementation(libs.kotlinx.datetime)
    implementation(libs.logback.logstash)
    implementation(libs.logback)

    api(libs.logger.fluentd)
    api(libs.logback.appenders)
}

