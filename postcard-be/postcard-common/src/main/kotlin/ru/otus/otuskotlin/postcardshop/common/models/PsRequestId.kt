package ru.otus.otuskotlin.postcardshop.common.models

@JvmInline
value class PsRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PsRequestId("")
    }
}