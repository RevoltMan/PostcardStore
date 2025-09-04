package ru.otus.otuskotlin.postcardshop.common.logging

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardFilter
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsError
import ru.otus.otuskotlin.postcardshop.common.models.PsRequestId
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId

fun PsContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "postcard",
    postcard = toPostcardLog(),
    errors = errors.map { it.toLog() },
)

private fun PsContext.toPostcardLog(): PsLogModel? {
    val postcardNone = Postcard()
    return PsLogModel(
        requestId = requestId.takeIf { it != PsRequestId.NONE }?.asString(),
        operation = command.toLogOperation(),
        requestPostcard = postcardRequest.takeIf { it != postcardNone }?.toLog(),
        responsePostcard = postcardResponse.takeIf { it != postcardNone }?.toLog(),
        responsePostcards = postcardsResponse.takeIf { it.isNotEmpty() }?.filter { it != postcardNone }?.map { it.toLog() },
        requestFilter = postcardFilterRequest.takeIf { it != PostcardFilter() }?.toLog(),
    ).takeIf { it != PsLogModel() }
}

private fun PsCommand.toLogOperation(): PsLogOperation =
    when(this) {
        PsCommand.NONE -> PsLogOperation.NONE
        PsCommand.CREATE -> PsLogOperation.CREATE
        PsCommand.READ -> PsLogOperation.READ
        PsCommand.SEARCH -> PsLogOperation.SEARCH
        PsCommand.DELETE -> PsLogOperation.DELETE
        PsCommand.UPDATE -> PsLogOperation.UPDATE
    }

private fun PostcardFilter.toLog() = PostcardFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    price = price.takeIf { it != 0 },
    ownerId = ownerId.takeIf { it != PsUserId.NONE }?.asString(),
    author = author.takeIf { it.isNotBlank() }
)

private fun PsError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun Postcard.toLog() = PostcardLog(
    id = id.takeIf { it != PostcardId.NONE }?.asString(),
    title = title,
    author = author,
    price = price,
    event = event,
    ownerId = ownerId.takeIf { it != PsUserId.NONE }?.asString() ?: "",
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet() ?: emptySet(),
)