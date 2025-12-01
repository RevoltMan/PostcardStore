package ru.otus.otuskotlin.postcardshop.repo.tests

import org.junit.Test
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsError
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardIdRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseErr
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPostcardReadTest {
    abstract val repo: RepoPostcard
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readPostcard(DbPostcardIdRequest(readSuccess.id))

        assertIs<DbPostcardResponseOk>(result)
        assertEquals(readSuccess, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readPostcard(DbPostcardIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbPostcardResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: PsError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitPostcards("read") {
        override val initObjects: List<Postcard> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = PostcardId("postcard-repo-read-notFound")

    }
}
