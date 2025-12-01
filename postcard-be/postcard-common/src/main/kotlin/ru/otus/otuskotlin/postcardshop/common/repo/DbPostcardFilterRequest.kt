package ru.otus.otuskotlin.postcardshop.common.repo

import ru.otus.otuskotlin.postcardshop.common.models.PsUserId

data class DbPostcardFilterRequest(
    val title: String = "",
    var author: String = "",
    var event: String = "",
    var price: Int? = null,
    val ownerId: PsUserId = PsUserId.NONE,
)