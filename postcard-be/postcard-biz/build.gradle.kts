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
    implementation(project(":postcard-be:postcard-repo-common"))
    implementation(project(":postcard-be:postcard-repo-inmemory"))
    implementation(project(":postcard-be:postcard-repo-tests"))

    implementation(libs.coroutines.test)

    testImplementation(libs.jUnit.api)
    testImplementation(libs.jUnit.engine)
    testImplementation(libs.jUnit.platform)
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}