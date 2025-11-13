package ru.otus.otuskotlin.postcardshop.repo.tests

import org.junit.Test
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardFilterRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoPostcardSearchTest {
    abstract val repo: RepoPostcard

    protected open val initializedObjects: List<Postcard> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchPostcard(DbPostcardFilterRequest(ownerId = searchOwnerId))
        assertIs<DbPostcardsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchPrice() = runRepoTest {
        val result = repo.searchPostcard(DbPostcardFilterRequest(price = 123))
        assertIs<DbPostcardsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() }) // Постомтреть
    }

    companion object: BaseInitPostcards("search") {

        val searchOwnerId = PsUserId("owner")
        override val initObjects: List<Postcard> = listOf(
            createInitTestModel("Postcard1"),
            createInitTestModel("Postcard2", ownerId = searchOwnerId),
            createInitTestModel("Postcard3", ownerId = searchOwnerId),

        )
    }
}