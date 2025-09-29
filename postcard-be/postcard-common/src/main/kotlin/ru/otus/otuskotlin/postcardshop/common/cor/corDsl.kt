package ru.otus.otuskotlin.postcardshop.common.cor

import ru.otus.otuskotlin.postcardshop.common.cor.handlers.CorChainDslImpl
import ru.otus.otuskotlin.postcardshop.common.cor.handlers.CorWorkerDslImpl

@CorDslMarker
interface CorExecDsl<T> {
    var title: String
    var description: String
    fun on(function: suspend T.() -> Boolean)
    fun except(function: suspend T.(e: Throwable) -> Unit)

    fun build(): CorExec<T>
}

@CorDslMarker
interface CorChainDsl<T> : CorExecDsl<T> {
    fun add(worker: CorExecDsl<T>)
}

@CorDslMarker
interface CorWorkerDsl<T> : CorExecDsl<T> {
    fun handle(function: suspend T.() -> Unit)
}

fun <T> rootChain(function: CorChainDsl<T>.() -> Unit): CorChainDsl<T> = CorChainDslImpl<T>().apply(function)


fun <T> CorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDslImpl<T>().apply(function))
}

fun <T> CorChainDsl<T>.worker(function: CorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDslImpl<T>().apply(function))
}

fun <T> CorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(CorWorkerDslImpl<T>().also {
        it.title = title
        it.description = description
        it.handle(blockHandle)
    })
}