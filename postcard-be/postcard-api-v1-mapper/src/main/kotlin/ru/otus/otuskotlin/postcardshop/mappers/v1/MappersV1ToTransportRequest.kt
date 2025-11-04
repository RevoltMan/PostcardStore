package ru.otus.otuskotlin.postcardshop.mappers.v1

import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateObject
import ru.otus.otuskotlin.postcardshop.common.models.Postcard


fun Postcard.toTransportCreatePostcard() = PostcardCreateObject(
    title = title,
    author = author,
    postcardEvent = event,
    price = price,
)

fun Postcard.toTransportReadPostcard() = PostcardReadObject(
    id = id.toTransportPostcard()
)

fun Postcard.toTransportUpdatePostcard() = PostcardUpdateObject(
    id = id.toTransportPostcard(),
    title = title,
    author = author,
    postcardEvent = event,
    price = price,
)


fun Postcard.toTransportDeletePostcard() = PostcardDeleteObject(
    id = id.toTransportPostcard(),
)