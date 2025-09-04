package ru.otus.otuskotlin.postcardshop.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.postcardshop.api.v1.models.IRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.IResponse
import ru.otus.otuskotlin.postcardshop.app.common.PostcardAppSettings
import ru.otus.otuskotlin.postcardshop.app.common.controllerHelper
import ru.otus.otuskotlin.postcardshop.mappers.v1.fromTransport
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportPostcard
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: PostcardAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {fromTransport(this@processV1.receive<Q>()) },
    { this@processV1.respond(toTransportPostcard() as R) },
    clazz,
    logId,
)