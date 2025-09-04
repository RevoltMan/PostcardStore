package ru.otus.otuskotlin.postcardshop.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.logging.PsLoggerProvider
import ru.otus.otuskotlin.postcardshop.common.logging.psLoggerLogback
import ru.otus.otuskotlin.postcardshop.ktor.PostcardAppSettingsImpl

fun Application.initAppSettings(): PostcardAppSettingsImpl {
    val corSettings = PsCorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return PostcardAppSettingsImpl(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = PsProcessor(corSettings),
    )
}

fun Application.getLoggerProviderConf(): PsLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> PsLoggerProvider { psLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp, socket and logback (default)")
    }