package ru.otus.otuskotlin.postcardshop.ktor.base

import ru.otus.otuskotlin.postcardshop.common.ws.PostcardWsSession
import ru.otus.otuskotlin.postcardshop.common.ws.PostcardWsSessionRepo

class KtorWsSessionRepo: PostcardWsSessionRepo {
    private val sessions: MutableSet<PostcardWsSession> = mutableSetOf()
    override fun add(session: PostcardWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: PostcardWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
