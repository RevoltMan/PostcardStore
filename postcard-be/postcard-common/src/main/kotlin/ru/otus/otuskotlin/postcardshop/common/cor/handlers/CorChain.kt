package ru.otus.otuskotlin.postcardshop.common.cor.handlers

import ru.otus.otuskotlin.postcardshop.common.cor.CorChainDsl
import ru.otus.otuskotlin.postcardshop.common.cor.CorDslMarker
import ru.otus.otuskotlin.postcardshop.common.cor.CorExec
import ru.otus.otuskotlin.postcardshop.common.cor.CorExecDsl

class CorChain<T>(
    private val execs: List<CorExec<T>>,
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) {
        execs.forEach {
            it.exec(context)
        }
    }
}

@CorDslMarker
class CorChainDslImpl<T>(
) : CorExecDslImpl<T>(), CorChainDsl<T> {
    private val workers: MutableList<CorExecDsl<T>> = mutableListOf()
    override fun add(worker: CorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): CorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}