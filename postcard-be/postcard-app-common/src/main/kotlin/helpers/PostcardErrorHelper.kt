package ru.otus.otuskotlin.postcardshop.app.common.helpers

import ru.otus.otuskotlin.postcardshop.common.models.PsError

fun Throwable.asPostcardError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = PsError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)