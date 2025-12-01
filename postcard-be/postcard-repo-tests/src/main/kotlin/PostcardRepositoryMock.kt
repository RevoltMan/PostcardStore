package ru.otus.otuskotlin.postcardshop.repo.tests

import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardFilterRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardIdRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponse
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponse
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard

class PostcardRepositoryMock(
    private val invokeCreatePostcard: (DbPostcardRequest) -> DbPostcardResponse = { DEFAULT_POSTCARD_SUCCESS_EMPTY_MOCK },
    private val invokeReadPostcard: (DbPostcardIdRequest) -> DbPostcardResponse = { DEFAULT_POSTCARD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdatePostcard: (DbPostcardRequest) -> DbPostcardResponse = { DEFAULT_POSTCARD_SUCCESS_EMPTY_MOCK },
    private val invokeDeletePostcard: (DbPostcardIdRequest) -> DbPostcardResponse = { DEFAULT_POSTCARD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchPostcard: (DbPostcardFilterRequest) -> DbPostcardsResponse = { DEFAULT_POSTCARDS_SUCCESS_EMPTY_MOCK },
): RepoPostcard {
    override suspend fun createPostcard(rq: DbPostcardRequest): DbPostcardResponse {
        return invokeCreatePostcard(rq)
    }

    override suspend fun readPostcard(rq: DbPostcardIdRequest): DbPostcardResponse {
        return invokeReadPostcard(rq)
    }

    override suspend fun updatePostcard(rq: DbPostcardRequest): DbPostcardResponse {
        return invokeUpdatePostcard(rq)
    }

    override suspend fun deletePostcard(rq: DbPostcardIdRequest): DbPostcardResponse {
        return invokeDeletePostcard(rq)
    }

    override suspend fun searchPostcard(rq: DbPostcardFilterRequest): DbPostcardsResponse {
        return invokeSearchPostcard(rq)
    }

    companion object {
        val DEFAULT_POSTCARD_SUCCESS_EMPTY_MOCK = DbPostcardResponseOk(Postcard())
        val DEFAULT_POSTCARDS_SUCCESS_EMPTY_MOCK = DbPostcardsResponseOk(emptyList())
    }
}
