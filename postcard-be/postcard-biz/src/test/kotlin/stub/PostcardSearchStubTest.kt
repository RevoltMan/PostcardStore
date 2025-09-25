package ru.otus.otuskotlin.postcardshop.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardFilter
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs

class PostcardSearchStubTest {
    private val processor = PsProcessor()
    val filter = PostcardFilter(searchString = "Малевич")

    @Test
    fun read() = runTest {

        val ctx = makeSearchStubContext(
            stubCase = PsStubs.SUCCESS,
        )
        processor.exec(ctx)
        assertTrue(ctx.postcardsResponse.size > 1)
        val first = ctx.postcardsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
    }

    @Test
    fun badId() = runTest {
        val ctx = makeSearchStubContext(    
            stubCase = PsStubs.BAD_ID,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = makeSearchStubContext(
            stubCase = PsStubs.DB_ERROR,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = makeSearchStubContext(
            stubCase = PsStubs.BAD_TITLE,
        )
        processor.exec(ctx)
        assertEquals(Postcard(), ctx.postcardResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
