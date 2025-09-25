package ru.otus.otuskotlin.postcardshop.biz.validation

import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand

abstract class BaseBizValidationTest {
    protected abstract val command: PsCommand
    private val settings by lazy { PsCorSettings() }
    protected val processor by lazy { PsProcessor(settings) }
}