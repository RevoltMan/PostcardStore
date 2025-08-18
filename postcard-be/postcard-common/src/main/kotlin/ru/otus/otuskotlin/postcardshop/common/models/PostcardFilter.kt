package ru.otus.otuskotlin.postcardshop.common.models

data class PostcardFilter(
    var searchString: String = "",
    var ownerId: PsUserId = PsUserId.NONE,
    var event: String = "",
    var author: String = "",
)
