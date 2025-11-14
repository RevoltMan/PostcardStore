package ru.otus.otuskotlin.postcardshop.repo.inmemory

import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId

data class PostcardEntity(
    var id: String? = null,
    var title: String? = null,
    var author: Set<String> = mutableSetOf(),
    var event: Set<String> = mutableSetOf(),
    var price: Int = 0,
    var ownerId: String? =null,
) {
    constructor(model: Postcard): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        author = model.author,
        event = model.event,
        price = model.price,
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
    )

    fun toInternal() = Postcard(
        id = id?.let { PostcardId(it) }?: PostcardId.NONE,
        title = title?: "",
        author = author,
        event = event,
        price = price,
        ownerId = ownerId?.let { PsUserId(it) }?: PsUserId.NONE,
    )
}