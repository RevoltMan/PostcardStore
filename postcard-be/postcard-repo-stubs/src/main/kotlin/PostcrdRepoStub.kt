package ru.otus.otuskotlin.postcardshop.repo.stub

import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardFilterRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardIdRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponse
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponse
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub

class PostcardRepoStub() : RepoPostcard {
    override suspend fun createPostcard(rq: DbPostcardRequest): DbPostcardResponse {
        return DbPostcardResponseOk(
            data = PostcardStub.get(),
        )
    }

    override suspend fun readPostcard(rq: DbPostcardIdRequest): DbPostcardResponse {
        return DbPostcardResponseOk(
            data = PostcardStub.get(),
        )
    }

    override suspend fun updatePostcard(rq: DbPostcardRequest): DbPostcardResponse {
        return DbPostcardResponseOk(
            data = PostcardStub.get(),
        )
    }

    override suspend fun deletePostcard(rq: DbPostcardIdRequest): DbPostcardResponse {
        return DbPostcardResponseOk(
            data = PostcardStub.get(),
        )
    }

    override suspend fun searchPostcard(rq: DbPostcardFilterRequest): DbPostcardsResponse {
        return DbPostcardsResponseOk(
            data = PostcardStub.prepareSearchList(filter = ""),
        )
    }
}