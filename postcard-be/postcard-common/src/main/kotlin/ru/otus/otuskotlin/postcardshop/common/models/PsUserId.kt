package ru.otus.otuskotlin.postcardshop.common.models

@JvmInline
value class PsUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PsUserId("")
    }
}