package ru.otus.otuskotlin.postcardshop.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.postcardshop.common.helpers.asPostcardError
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.logging.toLog
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import kotlin.reflect.KClass

suspend inline fun <T> PostcardAppSettings.controllerHelper(
    crossinline getRequest: suspend PsContext.() -> Unit,
    crossinline toResponse: suspend PsContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = PsContext(
        timeStart = Clock.System.now()
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (ex: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = PsState.FAILING
        ctx.errors.add(ex.asPostcardError())
        processor.exec(ctx)
        if (ctx.command == PsCommand.NONE) {
            ctx.command = PsCommand.READ
        }
        ctx.toResponse()
    }
}