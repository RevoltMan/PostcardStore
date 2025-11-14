package ru.otus.otuskotlin.postcardshop.common.repo

interface RepoPostcard {
    suspend fun createPostcard(rq: DbPostcardRequest): DbPostcardResponse
    suspend fun readPostcard(rq: DbPostcardIdRequest): DbPostcardResponse
    suspend fun updatePostcard(rq: DbPostcardRequest): DbPostcardResponse
    suspend fun deletePostcard(rq: DbPostcardIdRequest): DbPostcardResponse
    suspend fun searchPostcard(rq: DbPostcardFilterRequest): DbPostcardsResponse
    companion object {
        val NONE = object : RepoPostcard {
            override suspend fun createPostcard(rq: DbPostcardRequest): DbPostcardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readPostcard(rq: DbPostcardIdRequest): DbPostcardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updatePostcard(rq: DbPostcardRequest): DbPostcardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deletePostcard(rq: DbPostcardIdRequest): DbPostcardResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchPostcard(rq: DbPostcardFilterRequest): DbPostcardsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
