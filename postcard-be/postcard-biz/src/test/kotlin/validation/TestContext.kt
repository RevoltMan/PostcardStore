package ru.otus.otuskotlin.postcardshop.biz.validation

import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardFilter
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState.NONE
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode.TEST
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub

private val stub = PostcardStub.get()

internal fun makeTestContext(
    command: PsCommand,
    title: String = "Крик",
    id: PostcardId = stub.id,
    author: Set<String> = setOf("Рыжикова"),
    event: Set<String> = setOf("День города"),
    price: Int = 123,
    filter: PostcardFilter = PostcardFilter(),
) : PsContext =
    PsContext(
        command = command,
        state = NONE,
        workMode = TEST,
        postcardRequest = Postcard(
            id = id,
            title = title,
            author = author,
            event = event,
            price = price,
        ),
        postcardFilterRequest = filter
    )