package ru.otus.otuskotlin.postcardshop.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardFilter
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsError
import ru.otus.otuskotlin.postcardshop.common.models.PsRequestId
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode

import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs

data class PsContext(
    var command: PsCommand = PsCommand.NONE,
    var state: PsState = PsState.NONE,
    val errors: MutableList<PsError> = mutableListOf(),

    var workMode: PsWorkMode = PsWorkMode.PROD,
    var stubCase: PsStubs = PsStubs.NONE,

    var requestId: PsRequestId = PsRequestId.NONE,
    var timeStart: Instant? = null,
    var postcardRequest: Postcard = Postcard(),
    var postcardFilterRequest: PostcardFilter = PostcardFilter(),

    var postcardResponse: Postcard = Postcard(),
    var postcardsResponse: MutableList<Postcard> = mutableListOf(),
)