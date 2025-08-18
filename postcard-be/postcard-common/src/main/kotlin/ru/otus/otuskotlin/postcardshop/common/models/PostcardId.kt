package ru.otus.otuskotlin.postcardshop.common.models

@JvmInline
value class PostcardId (private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PostcardId("")
    }
}