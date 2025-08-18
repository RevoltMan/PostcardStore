package ru.otus.otuskotlin.postcardshop.common.models

data class PsError (
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)