package ru.otus.otuskotlin.postcardshop.biz.validation

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState.FAILING

fun validationPriceCorrect(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(
        command = command,
        price = 123,
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FAILING, ctx.state)
}

fun validationPriceLessZero(command: PsCommand, processor: PsProcessor) = runTest {
    val ctx = makeTestContext(
        command = command,
        price = 0,
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("price", error?.field)
    assertTrue(error!!.message.contains("price"))
}