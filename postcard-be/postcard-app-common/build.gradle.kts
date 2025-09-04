plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(project(":postcard-be:postcard-common"))
    implementation(project(":postcard-be:postcard-biz"))
}