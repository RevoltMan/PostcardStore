package ru.otus.otuskotlin.postcardshop.biz.validation

import biz.validateSearchStringLength
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.cor.rootChain
import ru.otus.otuskotlin.postcardshop.common.models.PostcardFilter
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.models.PsState.FAILING
import ru.otus.otuskotlin.postcardshop.common.models.PsState.RUNNING
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = PsCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = PsContext(
            command = command,
            state = PsState.NONE,
            workMode = PsWorkMode.TEST,
            postcardFilterRequest = PostcardFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(FAILING, ctx.state)
    }

    @Test
    fun emptyString() = runTest {
        val ctx = PsContext(state = RUNNING, postcardFilterRequest = PostcardFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = PsContext(state = RUNNING, postcardFilterRequest = PostcardFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = PsContext(state = RUNNING, postcardFilterRequest = PostcardFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = PsContext(state = RUNNING, postcardFilterRequest = PostcardFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = PsContext(state = RUNNING, postcardFilterRequest = PostcardFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}