package ru.otus.otuskotlin.postcardshop.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs

class PostcardReadStubTest {

    private val processor = PsProcessor()

    @Test
    fun read() = runTest {

        val ctx = makeReadStubContext(
            stubCase = PsStubs.SUCCESS,
        )
        processor.exec(ctx)
        val stub = PostcardStub.get()
        assertEquals(stub.id, ctx.postcardResponse.id)
        assertEquals(initPostcard().title, ctx.postcardResponse.title)
        assertEquals(stub.author, ctx.postcardResponse.author)
        assertEquals(stub.event, ctx.postcardResponse.event)
        assertEquals(stub.price, ctx.postcardResponse.price)
    }

    @Test
    fun badId() = runTest {
        val ctx = makeReadStubContext(
            stubCase = PsStubs.BAD_ID,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = makeReadStubContext(
            stubCase = PsStubs.DB_ERROR,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = makeReadStubContext(
            stubCase = PsStubs.BAD_TITLE,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
