package ru.otus.otuskotlin.postcardshop.common.repo

import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PsError

sealed interface DbPostcardResponse: DbResponse

data class DbPostcardResponseOk(
    val data: Postcard
): DbPostcardResponse

data class DbPostcardResponseErr(
    val errors: List<PsError> = emptyList()
): DbPostcardResponse {
    constructor(err: PsError): this(listOf(err))
}

data class DbPostcardResponseErrWithData(
    val data: Postcard,
    val errors: List<PsError> = emptyList()
): DbPostcardResponse {
    constructor(postcard: Postcard, err: PsError): this(postcard, listOf(err))
}