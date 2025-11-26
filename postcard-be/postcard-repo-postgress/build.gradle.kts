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
    implementation(libs.uuid)

    implementation(libs.db.postgres)
    implementation(libs.bundles.exposed)

    implementation(project(":postcard-be:postcard-repo-tests"))
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.logback)
    api(kotlin("test-junit"))
}