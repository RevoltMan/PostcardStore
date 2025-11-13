package ru.otus.otuskotlin.postcardshop.repo.tests

import org.junit.Test
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId
import ru.otus.otuskotlin.postcardshop.common.repo.*
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPostcardUpdateTest {
    abstract val repo: RepoPostcard
    protected open val updateSuccess = initObjects[0]
    protected open val updateConcurent = initObjects[1]
    protected val updateIdNotFound = PostcardId("Postcard-repo-update-not-found")

    private val reqUpdateSuccess by lazy {
        Postcard(
            id = updateSuccess.id,
            title = "update object",
            author = setOf("author"),
            event = setOf("event"),
            price = 123,
            ownerId = PsUserId("owner"),
        )
    }
    private val reqUpdateNotFound = Postcard(
        id = updateIdNotFound,
        title = "update object not found",
        author = setOf("author not found"),
        event = setOf("event not found"),
        price = 1234,
        ownerId = PsUserId("owner"),
    )
    private val reqUpdateConc by lazy {
        Postcard(
            id = updateConcurent.id,
            title = "update object not found",
            author = setOf("author not found"),
            event = setOf("event not found"),
            price = 1234,
            ownerId = PsUserId("owner"),
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updatePostcard(DbPostcardRequest(reqUpdateSuccess))
        println("ERRORS: ${(result as? DbPostcardResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbPostcardResponseErrWithData)?.errors}")
        assertIs<DbPostcardResponseOk>(result)
        assertEquals(reqUpdateSuccess.id, result.data.id)
        assertEquals(reqUpdateSuccess.title, result.data.title)
        assertEquals(reqUpdateSuccess.author, result.data.author)
        assertEquals(reqUpdateSuccess.event, result.data.event)
        assertEquals(reqUpdateSuccess.price, result.data.price)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updatePostcard(DbPostcardRequest(reqUpdateNotFound))
        assertIs<DbPostcardResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updatePostcard(DbPostcardRequest(reqUpdateConc))
        assertIs<DbPostcardResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConcurent, result.data)
    }

    companion object : BaseInitPostcards("update") {
        override val initObjects: List<Postcard> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConcurreny"),
        )
    }
}