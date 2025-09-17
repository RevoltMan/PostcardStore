package ru.otus.otuskotlin.postcardshop.mappers.v1

import ru.otus.otuskotlin.postcardshop.api.v1.models.Response
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardPermissions
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardResponseObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.ResponseResult
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.exceptions.UnknownPostcardCommand
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PostcardPermissionClient
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsError
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId

fun PsContext.toTransportPostcard(): Response = when (val cmd = command) {
    PsCommand.CREATE -> toTransportCreate()
    PsCommand.READ -> toTransportRead()
    PsCommand.UPDATE -> toTransportUpdate()
    PsCommand.DELETE -> toTransportDelete()
    PsCommand.SEARCH -> toTransportSearch()
    PsCommand.NONE -> throw UnknownPostcardCommand(cmd)
}

fun PsContext.toTransportCreate() = PostcardCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    postcard = postcardResponse.toTransportPostcard()
)

fun PsContext.toTransportRead() = PostcardReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    postcard = postcardResponse.toTransportPostcard()
)

fun PsContext.toTransportUpdate() = PostcardUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    postcard = postcardResponse.toTransportPostcard()
)

fun PsContext.toTransportDelete() = PostcardDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    postcard = postcardResponse.toTransportPostcard()
)

fun PsContext.toTransportSearch() = PostcardSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    postcards = postcardsResponse.toTransportPostcard()
)

fun List<Postcard>.toTransportPostcard(): List<PostcardResponseObject>? = this
    .map { it.toTransportPostcard() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun Postcard.toTransportPostcard(): PostcardResponseObject = PostcardResponseObject(
    id = id.toTransportPostcard(),
    title = title.takeIf { it.isNotBlank() },
    author = author.takeIf { it.isNotEmpty() },
    postcardEvent = event.takeIf { it.isNotEmpty() },
    price  = price,
    ownerId = ownerId.takeIf { it != PsUserId.NONE }?.asString(),
    permissions = permissionsClient.toTransportPostcard(),
)

internal fun PostcardId.toTransportPostcard() = takeIf { it != PostcardId.NONE }?.asString()

private fun Set<PostcardPermissionClient>.toTransportPostcard(): Set<PostcardPermissions>? = this
    .map { it.toTransportPostcard() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun PostcardPermissionClient.toTransportPostcard() = when (this) {
    PostcardPermissionClient.READ -> PostcardPermissions.READ
    PostcardPermissionClient.UPDATE -> PostcardPermissions.UPDATE
    PostcardPermissionClient.MAKE_VISIBLE_OWNER -> PostcardPermissions.MAKE_VISIBLE_OWN
    PostcardPermissionClient.MAKE_VISIBLE_GROUP -> PostcardPermissions.MAKE_VISIBLE_GROUP
    PostcardPermissionClient.MAKE_VISIBLE_PUBLIC -> PostcardPermissions.MAKE_VISIBLE_PUBLIC
    PostcardPermissionClient.DELETE -> PostcardPermissions.DELETE
}

private fun List<PsError>.toTransportErrors(): List<ru.otus.otuskotlin.postcardshop.api.v1.models.Error>? = this
    .map { it.toTransportPostcard() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun PsError.toTransportPostcard() = ru.otus.otuskotlin.postcardshop.api.v1.models.Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun PsState.toResult(): ResponseResult? = when (this) {
    PsState.RUNNING -> ResponseResult.SUCCESS
    PsState.FAILING -> ResponseResult.ERROR
    PsState.FINISHING -> ResponseResult.SUCCESS
    PsState.NONE -> null
}