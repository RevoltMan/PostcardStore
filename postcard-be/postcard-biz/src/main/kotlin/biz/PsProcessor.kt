package ru.otus.otuskotlin.postcardshop.biz

import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub

class PsProcessor(val corSettings: PsCorSettings) {

    suspend fun exec(ctx: PsContext) {
        ctx.postcardResponse = PostcardStub.get()
        ctx.postcardsResponse = PostcardStub.prepareSearchList("Postcard search").toMutableList()
        ctx.state = PsState.RUNNING
    }
}