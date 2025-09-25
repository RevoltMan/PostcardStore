package ru.otus.otuskotlin.postcardshop.app.common.cor

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.common.cor.CorExecDsl
import ru.otus.otuskotlin.postcardshop.common.cor.chain
import ru.otus.otuskotlin.postcardshop.common.cor.rootChain
import ru.otus.otuskotlin.postcardshop.common.cor.worker
import kotlin.test.assertFails

class CorDslTest {
    private suspend fun execute(dsl: CorExecDsl<TestContext>): TestContext {
        val ctx = TestContext()
        dsl.build().exec(ctx)
        return ctx
    }

    @Test
    fun `handle should execute`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { history += "world" }
            }
        }
        val ctx = execute(chain)
        assertEquals("world", ctx.history)
        assertEquals(TestContext.CorStatuses.NONE, ctx.status)
    }

    @Test
    fun `on should check condition`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                on { status == TestContext.CorStatuses.ERROR }
                handle { history += "world " }
            }
            worker {
                on { status == TestContext.CorStatuses.NONE }
                handle {
                    history += "wide "
                    status = TestContext.CorStatuses.FAILING
                }
            }
            worker {
                on { status == TestContext.CorStatuses.FAILING }
                handle { history += "web " }
            }
        }
        val ctx = execute(chain)
        assertEquals("wide web ", ctx.history)
        assertEquals(TestContext.CorStatuses.FAILING, ctx.status)
    }

    @Test
    fun `except should execute when exception`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                handle { throw RuntimeException("Ошибка") }
                except { history += it.message }
            }
        }
        val ctx = execute(chain)
        assertEquals("Ошибка", ctx.history)
    }

    @Test
    fun `should throw when exception and no except`() = runTest {
        val chain = rootChain<TestContext> {
            worker("throw") { throw RuntimeException("Ошибка") }
        }
        assertFails {
            execute(chain)
        }
    }

    @Test
    fun `complex chain example`() = runTest {
        val chain = rootChain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == TestContext.CorStatuses.NONE }
                handle { status = TestContext.CorStatuses.RUNNING }
                except { status = TestContext.CorStatuses.ERROR }
            }

            chain {
                on { status == TestContext.CorStatuses.RUNNING }

                worker(
                    title = "Обработчик",
                    description = "Пример"
                ) {
                    some += 42
                }
            }

        }.build()

        val ctx = TestContext()
        chain.exec(ctx)
        assertEquals("", ctx.history)
        assertEquals(42, ctx.some)
        assertEquals(TestContext.CorStatuses.RUNNING, ctx.status)
    }
}