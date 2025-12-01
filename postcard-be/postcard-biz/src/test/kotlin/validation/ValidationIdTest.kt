package ru.otus.otuskotlin.postcardshop.biz.validation

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState.FAILING

fun validationIdCorrect(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(
        command = command,
        id = PostcardId("ZXY-873"),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FAILING, ctx.state)
}

fun validationIdTrim(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(
        command = command,
        id = PostcardId(" \n\t ZXY-873 \n\t "),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FAILING, ctx.state)
}

fun validationIdEmpty(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(
        command = command,
        id = PostcardId("  "),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertTrue(error!!.message.contains("id"))
}

fun validationIdFormat(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(
        command = command,
        id = PostcardId(" !@#\$%^&*(),.{} "),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertTrue(error!!.message.contains("id"))
}