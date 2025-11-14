package ru.otus.otuskotlin.postcardshop.common.repo

import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PsError

sealed interface DbPostcardsResponse: DbResponse

data class DbPostcardsResponseOk(
    val data: List<Postcard>
): DbPostcardsResponse

@Suppress("unused")
data class DbPostcardsResponseErr(
    val errors: List<PsError> = emptyList()
): DbPostcardsResponse {
    constructor(err: PsError): this(listOf(err))
}