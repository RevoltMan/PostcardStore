plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("org.openapi.generator") version "7.12.0" apply false
}
rootProject.name = "PostcardStore"

include("postcard-be")
include("postcard-be:postcard-api-v1")
findProject(":postcard-be:postcard-api-v1")?.name = "postcard-api-v1"
include("postcard-be:postcard-common")
findProject(":postcard-be:postcard-common")?.name = "postcard-common"
include("postcard-be:postcard-api-v1-mapper")
findProject(":postcard-be:postcard-api-v1-mapper")?.name = "postcard-api-v1-mapper"
