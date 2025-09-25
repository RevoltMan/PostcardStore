package ru.otus.otuskotlin.postcardshop.biz

import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.cor.CorChainDsl
import ru.otus.otuskotlin.postcardshop.common.cor.worker
import ru.otus.otuskotlin.postcardshop.common.helpers.fail
import ru.otus.otuskotlin.postcardshop.common.logging.LogLevel
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsError
import ru.otus.otuskotlin.postcardshop.common.models.PsState.FINISHING
import ru.otus.otuskotlin.postcardshop.common.models.PsState.RUNNING
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.BAD_AUTHOR
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.BAD_ID
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.BAD_PRICE
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.BAD_SEARCH_STRING
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.BAD_TITLE
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.CANNOT_DELETE
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.DB_ERROR
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.NOT_FOUND
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs.SUCCESS

fun CorChainDsl<PsContext>.stubCreateContext(
    title: String,
    corSettings: PsCorSettings,) = worker {
    this.title = title
    this.description = "Кейс создания открытки"
    on { stubCase == SUCCESS && state == RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = FINISHING
            val stub = PostcardStub.prepareResult {
                postcardRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                postcardRequest.event.takeIf { it.isNotEmpty() }?.also { this.event = it }
                postcardRequest.author.takeIf { it.isNotEmpty() }?.also { this.author = it }
                postcardRequest.id.takeIf { it != PostcardId.NONE }?.also { this.id = it }
                postcardRequest.price.takeIf { it != 0 }?.also { this.price = it }
                postcardRequest.ownerId.takeIf { it != PsUserId.NONE }?.also { this.ownerId = it }
                postcardRequest.permissionsClient.takeIf { it.isNotEmpty() }?.also { this.permissionsClient = it }
            }
            postcardResponse = stub
        }
    }
}

fun CorChainDsl<PsContext>.stubReadContext(title: String, corSettings: PsCorSettings,) = worker {
    this.title = title
    this.description = "Кейс чтения открытки"
    on { stubCase == SUCCESS && state == RUNNING }
    val logger = corSettings.loggerProvider.logger("stubReadSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = FINISHING
            val stub = PostcardStub.prepareResult {
                postcardRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
            postcardResponse = stub
        }
    }
}

fun CorChainDsl<PsContext>.stubUpdateSuccess(title: String, corSettings: PsCorSettings) = worker {
    this.title = title
    this.description = "Кейс успешного изменения данных открытки"
    on { stubCase == SUCCESS && state == RUNNING }
    val logger = corSettings.loggerProvider.logger("stubUpdateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = FINISHING
            val stub = PostcardStub.prepareResult {
                postcardRequest.id.takeIf { it != PostcardId.NONE }?.also { this.id = it }
                postcardRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                postcardRequest.event.takeIf { it.isNotEmpty() }?.also { this.event = it }
                postcardRequest.author.takeIf { it.isNotEmpty() }?.also { this.author = it }
                postcardRequest.id.takeIf { it != PostcardId.NONE }?.also { this.id = it }
                postcardRequest.price.takeIf { it != 0 }?.also { this.price = it }
                postcardRequest.ownerId.takeIf { it != PsUserId.NONE }?.also { this.ownerId = it }
                postcardRequest.permissionsClient.takeIf { it.isNotEmpty() }?.also { this.permissionsClient = it }
            }
            postcardResponse = stub
        }
    }
}

fun CorChainDsl<PsContext>.stubDeleteSuccess(title: String, corSettings: PsCorSettings) = worker {
    this.title = title
    this.description = "Кейс успешного удаления открытки"
    on { stubCase == SUCCESS && state == RUNNING }
    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = FINISHING
            val stub = PostcardStub.prepareResult {
                postcardRequest.id.takeIf { it != PostcardId.NONE }?.also { this.id = it }
            }
            postcardResponse = stub
        }
    }
}

fun CorChainDsl<PsContext>.stubSearchSuccess(title: String, corSettings: PsCorSettings) = worker {
    this.title = title
    this.description = "Кейс успешного поиска открыток"
    on { stubCase == SUCCESS && state == RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = FINISHING
            postcardsResponse.addAll(PostcardStub.prepareSearchList(postcardFilterRequest.searchString))
        }
    }
}


fun CorChainDsl<PsContext>.stubValidationBadTitle(title: String,) = worker {
    this.title = title
    this.description = "Кейс ошибка валидации заголовка"
    on { stubCase == BAD_TITLE && state == RUNNING }
    handle {
        fail(
            PsError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field",
            )
        )
    }
}

fun CorChainDsl<PsContext>.stubValidationBadAuthor(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки валидации имени автора"
    on { stubCase == BAD_AUTHOR && state == RUNNING }
    handle {
        fail(
            PsError(
                group = "validation",
                code = "validation-author",
                field = "authors",
                message = "Wrong authors field"
            )
        )
    }
}

fun CorChainDsl<PsContext>.stubValidationBadPrice(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки валидации цены"
    on { stubCase == BAD_PRICE && state == RUNNING }
    handle {
        fail(
            PsError(
                group = "validation",
                code = "validation-price",
                field = "price",
                message = "Wrong price field"
            )
        )
    }
}

fun CorChainDsl<PsContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки валидации для идентификатора открытки"
    on { stubCase == BAD_ID && state == RUNNING }
    handle {
        fail(
            PsError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}

fun CorChainDsl<PsContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки базы данных"
    on { stubCase == DB_ERROR && state == RUNNING }
    handle {
        fail(
            PsError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}

fun CorChainDsl<PsContext>.stubValidationCanNotDelete(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки при удалении данных из базы данных"
    on { stubCase == CANNOT_DELETE && state == RUNNING }
    handle {
        fail(
            PsError(
                group = "internal",
                code = "internal-db-delete",
                message = "Internal error"
            )
        )
    }
}

fun CorChainDsl<PsContext>.stubValidationNotFound(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки запись не найдена"
    on { stubCase == NOT_FOUND && state == RUNNING }
    handle {
        fail(
            PsError(
                group = "internal",
                code = "not found",
                message = "Internal error"
            )
        )
    }
}

fun CorChainDsl<PsContext>.stubValidationBadSearchString(title: String) = worker {
    this.title = title
    this.description = "Кейс ошибки плохой строки поиска"
    on { stubCase == BAD_SEARCH_STRING && state == RUNNING }
    handle {
        fail(
            PsError(
                group = "internal",
                code = "search",
                message = "Internal error"
            )
        )
    }
}

fun CorChainDsl<PsContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = "Запрошен кейс, который не поддерживается в заглушках"
    on { state == RUNNING }
    handle {
        fail(
            PsError(
                group = "validation",
                code = "validation-main",
                field = "stub",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
