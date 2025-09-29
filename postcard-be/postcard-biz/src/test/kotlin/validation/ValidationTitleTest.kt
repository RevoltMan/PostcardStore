package ru.otus.otuskotlin.postcardshop.biz.validation

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState.FAILING

fun validationTitleCorrect(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(command, title = "Крик",)
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FAILING, ctx.state)
    assertEquals("Крик", ctx.postcardRequest.title)
    assertEquals(123, ctx.postcardRequest.price)
}

fun validationTitleTrim(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(command, title = " \t\n Крик \t\n ",)
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FAILING, ctx.state)
    assertEquals("Крик", ctx.postcardRequest.title)
    assertEquals(123, ctx.postcardRequest.price)
}

fun validationTitleEmpty(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(command, title = " ",)
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertTrue(error!!.message.contains("title"))
}

