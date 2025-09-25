package ru.otus.otuskotlin.postcardshop.biz.stub

import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs

internal fun initPostcard() =
    Postcard(
        id = PostcardId("Барабашка"),
        title = "Hello World",
        author = setOf("Васнецова"),
        event = setOf("годовщина"),
        price = 100,
    )

internal fun makeGlobalStubContext(command: PsCommand, stubCase: PsStubs): PsContext =
    PsContext(
        command = command,
        state = PsState.NONE,
        workMode = PsWorkMode.STUB,
        stubCase = stubCase,
        postcardRequest = initPostcard()
)

internal fun makeCreateStubContext(stubCase: PsStubs): PsContext =
    makeGlobalStubContext(PsCommand.CREATE, stubCase)

internal fun makeDeleteStubContext(stubCase: PsStubs): PsContext =
    makeGlobalStubContext(PsCommand.DELETE, stubCase)

internal fun makeReadStubContext(stubCase: PsStubs): PsContext =
    makeGlobalStubContext(PsCommand.READ, stubCase)

internal fun makeSearchStubContext(stubCase: PsStubs): PsContext =
    makeGlobalStubContext(PsCommand.SEARCH, stubCase)

internal fun makeUpdateStubContext(stubCase: PsStubs): PsContext =
    makeGlobalStubContext(PsCommand.UPDATE, stubCase)