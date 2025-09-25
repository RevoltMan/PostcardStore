package cor

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.app.common.cor.TestContext
import ru.otus.otuskotlin.postcardshop.common.cor.handlers.CorChain
import ru.otus.otuskotlin.postcardshop.common.cor.handlers.CorWorker
import kotlin.test.assertEquals

class CorChainTest {
    @Test
    fun `chain should execute workers`() = runTest {
        val createWorker = { title: String ->
            CorWorker<TestContext>(
                title = title,
                blockOn = { status == TestContext.CorStatuses.NONE },
                blockHandle = { history += "$title " }
            )
        }
        val chain = CorChain<TestContext>(
            execs = listOf(createWorker("Hello"), createWorker("World")),
            title = "Цепочка",
        )
        val ctx = TestContext()
        chain.exec(ctx)
        assertEquals("Hello World ", ctx.history)
        assertEquals(TestContext.CorStatuses.NONE, ctx.status)
    }
}