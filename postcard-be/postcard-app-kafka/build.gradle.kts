plugins {
    application
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}


dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation(project(":postcard-be:postcard-api-v1"))
    implementation(project(":postcard-be:postcard-common"))
    implementation(project(":postcard-be:postcard-app-common"))
    implementation(project(":postcard-be:postcard-biz"))
    implementation(project(":postcard-be:postcard-api-v1-mapper"))

    // logic
    implementation(project(":postcard-be:postcard-biz"))

    testImplementation(libs.jUnit.api)
    testImplementation(libs.jUnit.engine)
    testImplementation(libs.jUnit.platform)
}


application {
    mainClass.set("ru.otus.otuskotlin.postcardshop.kafka.MainKt")
}


tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}


