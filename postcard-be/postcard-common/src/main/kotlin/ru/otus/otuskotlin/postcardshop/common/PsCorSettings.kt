package ru.otus.otuskotlin.postcardshop.common

import ru.otus.otuskotlin.postcardshop.common.logging.PsLoggerProvider
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import ru.otus.otuskotlin.postcardshop.common.ws.PostcardWsSessionRepo

data class PsCorSettings(
    val loggerProvider: PsLoggerProvider = PsLoggerProvider(),
    val wsSessions: PostcardWsSessionRepo = PostcardWsSessionRepo.NONE,
    val repoStub: RepoPostcard = RepoPostcard.NONE,
    val repoTest: RepoPostcard = RepoPostcard.NONE,
    val repoProd: RepoPostcard = RepoPostcard.NONE,
) {
    companion object {
        val NONE = PsCorSettings()
    }
}