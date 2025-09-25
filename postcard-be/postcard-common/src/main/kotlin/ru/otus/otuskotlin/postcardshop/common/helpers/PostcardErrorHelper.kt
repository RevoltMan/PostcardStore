package ru.otus.otuskotlin.postcardshop.common.helpers

import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.logging.LogLevel
import ru.otus.otuskotlin.postcardshop.common.models.PsError
import ru.otus.otuskotlin.postcardshop.common.models.PsState

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

inline fun PsContext.addError(vararg error: PsError) = errors.addAll(error)

inline fun PsContext.fail(error: PsError) {
    addError(error)
    state = PsState.FAILING
}

inline fun errorValidation(
    field: String,
    validationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = PsError(
    code = "validation-$field-$validationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)