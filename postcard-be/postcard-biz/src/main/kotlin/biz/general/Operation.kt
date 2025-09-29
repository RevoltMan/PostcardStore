package ru.otus.otuskotlin.postcardshop.biz.general

import ru.otus.otuskotlin.postcardshop.biz.stubDbError
import ru.otus.otuskotlin.postcardshop.biz.stubNoCase
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.cor.CorChainDsl
import ru.otus.otuskotlin.postcardshop.common.cor.chain
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState.RUNNING
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode

fun CorChainDsl<PsContext>.operation(
    title: String,
    command: PsCommand,
    action: CorChainDsl<PsContext>.() -> Unit ) =
    chain {
        this.title = title
        on { this.command == command && state == RUNNING }
        action()
    }

fun CorChainDsl<PsContext>.stubs(
    title: String,
    block: CorChainDsl<PsContext>.() -> Unit ) =
    chain {
        block()
        this.title = title
        on { workMode == PsWorkMode.STUB && state == RUNNING }
        stubDbError("Имитация ошибки работы с БД")
        stubNoCase("Ошибка: запрошенная заглушка недопустима")
    }