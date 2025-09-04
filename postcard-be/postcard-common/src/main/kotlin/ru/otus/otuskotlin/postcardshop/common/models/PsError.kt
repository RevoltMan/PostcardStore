package ru.otus.otuskotlin.postcardshop.common.models

import ru.otus.otuskotlin.postcardshop.common.logging.LogLevel

data class PsError (
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)