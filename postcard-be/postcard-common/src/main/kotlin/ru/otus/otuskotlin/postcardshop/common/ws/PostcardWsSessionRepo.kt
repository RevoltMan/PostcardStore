package ru.otus.otuskotlin.postcardshop.common.ws

interface PostcardWsSessionRepo {
    fun add(session: PostcardWsSession)
    fun clearAll()
    fun remove(session: PostcardWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : PostcardWsSessionRepo {
            override fun add(session: PostcardWsSession) {}
            override fun clearAll() {}
            override fun remove(session: PostcardWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}