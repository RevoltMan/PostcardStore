package ru.otus.otuskotlin.postcardshop.biz.validation

import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand

class BizValidationUpdateTest : BaseBizValidationTest() {
    override val command: PsCommand = PsCommand.UPDATE

    @Test
    fun correctTitle() = validationTitleCorrect(command, processor)
    @Test
    fun trimTitle() = validationTitleTrim(command, processor)
    @Test
    fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test
    fun correctAuthor() = validationAuthorCorrect(command, processor)
    @Test
    fun emptyAuthor() = validationAuthorEmpty(command, processor)
    @Test
    fun correctEvent() = validationEventCorrect(command, processor)
    @Test
    fun emptyEvent() = validationEventEmpty(command, processor)
    @Test
    fun correctPrice() = validationPriceCorrect(command, processor)
    @Test
    fun priceZero() = validationPriceLessZero(command, processor)
    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun trimId() = validationIdTrim(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badFormatId() = validationIdFormat(command, processor)
}