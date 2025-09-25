package ru.otus.otuskotlin.postcardshop.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs

class PostcardUpdateStubTest {

    private val processor = PsProcessor()

    @Test
    fun create() = runTest {

        val ctx = makeUpdateStubContext(
            stubCase = PsStubs.SUCCESS,
        )
        processor.exec(ctx)
        assertEquals(initPostcard().id, ctx.postcardResponse.id)
        assertEquals(initPostcard().title, ctx.postcardResponse.title)
        assertEquals(initPostcard().author, ctx.postcardResponse.author)
        assertEquals(initPostcard().event, ctx.postcardResponse.event)
        assertEquals(initPostcard().price, ctx.postcardResponse.price)
    }

    @Test
    fun badId() = runTest {
        val ctx = makeUpdateStubContext(
            stubCase = PsStubs.BAD_ID,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = makeUpdateStubContext(
            stubCase = PsStubs.BAD_TITLE,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    
    @Test
    fun badAuthor() = runTest {
        val ctx = makeUpdateStubContext(
            stubCase = PsStubs.BAD_AUTHOR,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("authors", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badPrice() = runTest {
        val ctx = makeUpdateStubContext(
            stubCase = PsStubs.BAD_PRICE,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("price", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = makeUpdateStubContext(
            stubCase = PsStubs.DB_ERROR,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = makeUpdateStubContext(
            stubCase = PsStubs.BAD_SEARCH_STRING,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
