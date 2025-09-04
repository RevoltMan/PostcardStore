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
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.headers.response)
    implementation(libs.ktor.server.headers.caching)
    implementation(libs.ktor.server.headers.default)
    implementation(libs.ktor.server.cors)
    implementation(project(":postcard-be:postcard-api-v1"))
    implementation(project(":postcard-be:postcard-api-v1-mapper"))
    implementation(project(":postcard-be:postcard-common"))
    implementation(project(":postcard-be:postcard-app-common"))
    implementation(project(":postcard-be:postcard-biz"))

    testImplementation(libs.jUnit.api)
    testImplementation(libs.jUnit.engine)
    testImplementation(libs.jUnit.platform)
    testImplementation(libs.ktor.server.test.host)
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}