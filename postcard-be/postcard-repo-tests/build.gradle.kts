plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":postcard-be:postcard-common"))
    implementation(project(":postcard-be:postcard-repo-common"))

    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)

    api(kotlin("test-junit"))
    api(libs.coroutines.test)
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}