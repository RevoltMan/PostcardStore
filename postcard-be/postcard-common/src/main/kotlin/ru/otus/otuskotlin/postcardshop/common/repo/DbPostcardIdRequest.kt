package ru.otus.otuskotlin.postcardshop.common.repo

import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId

data class DbPostcardIdRequest (
    val id: PostcardId,
) {
    constructor(postcard: Postcard): this(postcard.id)
}