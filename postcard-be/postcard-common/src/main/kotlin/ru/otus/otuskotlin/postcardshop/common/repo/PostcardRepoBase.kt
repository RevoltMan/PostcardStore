package ru.otus.otuskotlin.postcardshop.common.repo

import ru.otus.otuskotlin.postcardshop.common.helpers.errorSystem

abstract class PostcardRepoBase: RepoPostcard {
    protected suspend fun tryPostcardMethod(block: suspend () -> DbPostcardResponse) = try {
        block()
    } catch (th: Throwable) {
        DbPostcardResponseErr(errorSystem("methodException", th = th))
    }

    protected suspend fun tryPostcardsMethod(block: suspend () -> DbPostcardsResponse) = try {
        block()
    } catch (th: Throwable) {
        DbPostcardsResponseErr(errorSystem("methodException", th = th))
    }
}