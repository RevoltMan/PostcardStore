package ru.otus.otuskotlin.postcardshop.common.ws

interface PostcardWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : PostcardWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}