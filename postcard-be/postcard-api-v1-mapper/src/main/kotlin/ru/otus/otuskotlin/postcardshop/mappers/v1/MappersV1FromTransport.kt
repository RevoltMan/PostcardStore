package ru.otus.otuskotlin.postcardshop.mappers.v1

import ru.otus.otuskotlin.postcardshop.api.v1.models.IRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDebug
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugMode
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugStubs
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchFilter
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateRequest
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardFilter
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs
import ru.otus.otuskotlin.postcardshop.ru.otus.otuskotlin.postcardshop.mappers.v1.exception.UnknownRequestClass

fun PsContext.fromTransport(request: IRequest) = when (request) {
    is PostcardCreateRequest -> fromTransport(request)
    is PostcardReadRequest -> fromTransport(request)
    is PostcardUpdateRequest -> fromTransport(request)
    is PostcardDeleteRequest -> fromTransport(request)
    is PostcardSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toPostcardId() = this?.let { PostcardId(it) } ?: PostcardId.NONE

private fun PostcardDebug?.transportToWorkMode(): PsWorkMode = when (this?.mode) {
    PostcardRequestDebugMode.PROD -> PsWorkMode.PROD
    PostcardRequestDebugMode.TEST -> PsWorkMode.TEST
    PostcardRequestDebugMode.STUB -> PsWorkMode.STUB
    null -> PsWorkMode.PROD
}

private fun PostcardDebug?.transportToStubCase(): PsStubs = when (this?.stub) {
    PostcardRequestDebugStubs.SUCCESS -> PsStubs.SUCCESS
    PostcardRequestDebugStubs.NOT_FOUND -> PsStubs.NOT_FOUND
    PostcardRequestDebugStubs.BAD_ID -> PsStubs.BAD_ID
    PostcardRequestDebugStubs.BAD_TITLE -> PsStubs.BAD_TITLE
    PostcardRequestDebugStubs.BAD_AUTHOR -> PsStubs.BAD_AUTHOR
    PostcardRequestDebugStubs.BAD_PRICE -> PsStubs.BAD_PRICE
    PostcardRequestDebugStubs.CANNOT_DELETE -> PsStubs.CANNOT_DELETE
    PostcardRequestDebugStubs.BAD_SEARCH_STRING -> PsStubs.BAD_SEARCH_STRING
    PostcardRequestDebugStubs.DB_ERROR -> PsStubs.DB_ERROR
    null -> PsStubs.NONE
}

fun PsContext.fromTransport(request: PostcardCreateRequest) {
    command = PsCommand.CREATE
    postcardRequest = request.postcard?.toInternal() ?: Postcard()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun PsContext.fromTransport(request: PostcardReadRequest) {
    command = PsCommand.READ
    postcardRequest = request.postcard.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PostcardReadObject?.toInternal(): Postcard =
    if (this != null) {
        Postcard(id = id.toPostcardId())
    } else {
        Postcard()
    }


fun PsContext.fromTransport(request: PostcardUpdateRequest) {
    command = PsCommand.UPDATE
    postcardRequest = request.postcard?.toInternal() ?: Postcard()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun PsContext.fromTransport(request: PostcardDeleteRequest) {
    command = PsCommand.DELETE
    postcardRequest = request.postcard.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PostcardDeleteObject?.toInternal(): Postcard =
    if (this != null) {
        Postcard(
            id = id.toPostcardId(),
        )
    } else {
        Postcard()
    }

fun PsContext.fromTransport(request: PostcardSearchRequest) {
    command = PsCommand.SEARCH
    postcardFilterRequest = request.adFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun PostcardSearchFilter?.toInternal(): PostcardFilter = PostcardFilter(
    searchString = this?.searchString ?: ""
)

private fun PostcardCreateObject.toInternal(): Postcard = Postcard(
    title = this.title ?: "",
    author = this.author ?: mutableSetOf(),
    event = this.postcardEvent ?: mutableSetOf(),
    price = this.price ?: 0
)

private fun PostcardUpdateObject.toInternal(): Postcard = Postcard(
    id = this.id.toPostcardId(),
    title = this.title ?: "",
    author = this.author ?: mutableSetOf(),
    event = this.postcardEvent ?: mutableSetOf(),
    price = this.price ?: 0,
)
