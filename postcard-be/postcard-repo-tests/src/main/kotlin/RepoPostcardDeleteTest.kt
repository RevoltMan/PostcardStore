package ru.otus.otuskotlin.postcardshop.repo.tests

import org.junit.Test
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.repo.*
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoPostcardDeleteTest {
    abstract val repo: RepoPostcard
    protected open val deleteSuccess = initObjects[0]
    protected open val deleteConcarrent = initObjects[1]
    protected open val notFoundId = PostcardId("postcard-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deletePostcard(DbPostcardIdRequest(deleteSuccess.id))
        assertIs<DbPostcardResponseOk>(result)
        assertEquals(deleteSuccess.title, result.data.title)
        assertEquals(deleteSuccess.price, result.data.price)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readPostcard(DbPostcardIdRequest(notFoundId))

        assertIs<DbPostcardResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deletePostcard(DbPostcardIdRequest(deleteConcarrent.id))

        assertIs<DbPostcardResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitPostcards("delete") {
        override val initObjects: List<Postcard> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
