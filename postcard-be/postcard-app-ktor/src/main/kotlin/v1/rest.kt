package ru.otus.otuskotlin.postcardshop.ktor.v1


import io.ktor.server.routing.*
import ru.otus.otuskotlin.postcardshop.app.common.PostcardAppSettings


fun Route.v1Postcard(appSettings: PostcardAppSettings) {
    route("admin") {
        post("create") {
            call.createPostcard(appSettings)
        }
        post("update") {
            call.updatePostcard(appSettings)
        }
        post("delete") {
            call.deletePostcard(appSettings)
        }
    }
    route("postcard") {
        post("read") {
            call.readPostcard(appSettings)
        }
        post("search") {
            call.searchPostcard(appSettings)
        }
    }
}
