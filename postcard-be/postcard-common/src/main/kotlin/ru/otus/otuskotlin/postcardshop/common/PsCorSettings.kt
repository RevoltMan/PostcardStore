package ru.otus.otuskotlin.postcardshop.common

import ru.otus.otuskotlin.postcardshop.common.logging.PsLoggerProvider

data class PsCorSettings(
    val loggerProvider: PsLoggerProvider = PsLoggerProvider(),
) {
    companion object {
        val NONE = PsCorSettings()
    }
}