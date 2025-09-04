package ru.otus.otuskotlin.postcardshop.ktor.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateResponse
import ru.otus.otuskotlin.postcardshop.app.common.PostcardAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createPostcard::class
suspend fun ApplicationCall.createPostcard(appSettings: PostcardAppSettings) =
    processV1<PostcardCreateRequest, PostcardCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readPostcard::class
suspend fun ApplicationCall.readPostcard(appSettings: PostcardAppSettings) =
    processV1<PostcardReadRequest, PostcardReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updatePostcard::class
suspend fun ApplicationCall.updatePostcard(appSettings: PostcardAppSettings) =
    processV1<PostcardUpdateRequest, PostcardUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deletePostcard::class
suspend fun ApplicationCall.deletePostcard(appSettings: PostcardAppSettings) =
    processV1<PostcardDeleteRequest, PostcardDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchPostcard::class
suspend fun ApplicationCall.searchPostcard(appSettings: PostcardAppSettings) =
    processV1<PostcardSearchRequest, PostcardSearchResponse>(appSettings, clSearch, "search")
