package ru.otus.otuskotlin.postcardshop.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs

internal class PostcardDeleteStubTest {

    private val processor = PsProcessor()

    @Test
    fun delete() = runTest {

        val ctx = makeDeleteStubContext(
                stubCase = PsStubs.SUCCESS,
                )
        processor.exec(ctx)

        val stub = PostcardStub.get()
        assertEquals(initPostcard().id, ctx.postcardResponse.id)
        assertEquals(stub.title, ctx.postcardResponse.title)
        assertEquals(stub.author, ctx.postcardResponse.author)
        assertEquals(stub.event, ctx.postcardResponse.event)
        assertEquals(stub.price, ctx.postcardResponse.price)
    }

    @Test
    fun badId() = runTest {
        val ctx = makeDeleteStubContext(
                stubCase = PsStubs.BAD_ID,
                )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = makeDeleteStubContext(
                stubCase = PsStubs.DB_ERROR,
                )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = makeDeleteStubContext(
                stubCase = PsStubs.BAD_TITLE,
                )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}