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
    implementation(project(":postcard-be:postcard-repo-tests"))

    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)

    testImplementation(libs.jUnit.api)
    testImplementation(libs.jUnit.engine)
    testImplementation(libs.jUnit.platform)
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}