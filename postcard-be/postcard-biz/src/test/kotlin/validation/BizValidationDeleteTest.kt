package ru.otus.otuskotlin.postcardshop.biz.validation

import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand

class BizValidationDeleteTest: BaseBizValidationTest() {
    override val command: PsCommand = PsCommand.DELETE

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun idFormat() = validationIdFormat(command, processor)
}