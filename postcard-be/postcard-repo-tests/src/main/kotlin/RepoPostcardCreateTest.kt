package ru.otus.otuskotlin.postcardshop.repo.tests

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.repo.common.RepoPostcardInitializable
import kotlin.test.assertIs

abstract class RepoPostcardCreateTest {
    abstract val repo: RepoPostcardInitializable

    private val createObj = Postcard(
        title = "create object",
        author = setOf("Василиса"),
        event = setOf("Праздник"),
        price = 123,
        ownerId = PsUserId("owner"),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createPostcard(DbPostcardRequest(createObj))
        val expected = createObj
        assertIs<DbPostcardResponseOk>(result)
        assertNotEquals(PostcardId.NONE, result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.author, result.data.author)
        assertEquals(expected.event, result.data.event)
        assertEquals(expected.price, result.data.price)
        assertNotEquals(PostcardId.NONE, result.data.id)
    }

    companion object : BaseInitPostcards("create") {
        override val initObjects: List<Postcard> = emptyList()
    }
}