package ru.otus.otuskotlin.postcardshop.app.common.cor

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.common.cor.handlers.CorWorker

class CorWorkerTest {
    @Test
    fun `worker should execute handle`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "Заголовок",
            blockHandle = {
                history += " world "
                status = TestContext.CorStatuses.RUNNING
            }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals(" world ", ctx.history)
        assertEquals(TestContext.CorStatuses.RUNNING, ctx.status)
    }

    @Test
    fun `worker should not execute when off`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "Заголовок",
            blockOn = { status == TestContext.CorStatuses.ERROR },
            blockHandle = { history += " world " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("", ctx.history)
        assertEquals(TestContext.CorStatuses.NONE, ctx.status)
    }

    @Test
    fun `worker should handle exception`() = runTest {
        val worker = CorWorker<TestContext>(
            title = "Заголовок",
            blockHandle = { throw RuntimeException("Ошибка исполнения") },
            blockExcept = { e ->
                history += e.message
                status = TestContext.CorStatuses.ERROR
            }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("Ошибка исполнения", ctx.history)
        assertEquals(TestContext.CorStatuses.ERROR, ctx.status)
    }
}
