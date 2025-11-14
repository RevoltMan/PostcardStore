package ru.otus.otuskotlin.postcardshop.common.repo

import ru.otus.otuskotlin.postcardshop.common.models.Postcard

data class DbPostcardRequest (
    val postcard: Postcard
)