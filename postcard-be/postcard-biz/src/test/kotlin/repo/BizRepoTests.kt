package repo

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.biz.repo.initPostcard
import ru.otus.otuskotlin.postcardshop.biz.repo.initRepoContext
import ru.otus.otuskotlin.postcardshop.biz.repo.repoNotFoundTest
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardFilter
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState.FINISHING
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseOk
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubRose
import ru.otus.otuskotlin.postcardshop.repo.tests.PostcardRepositoryMock
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BizRepoTests {
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = PostcardRepositoryMock(
        invokeCreatePostcard = {
            DbPostcardResponseOk(
                data = Postcard(
                    id = PostcardId(uuid),
                    title = it.postcard.title,
                    author = it.postcard.author,
                    event = it.postcard.event,
                    price = it.postcard.price,
                    ownerId = PsStubRose.PS_ROSE1.ownerId,
                )
            )
        },
        invokeReadPostcard = {
            DbPostcardResponseOk(
                data = Postcard(
                    id = initPostcard.id,
                    title = initPostcard.title,
                    author = initPostcard.author,
                    event = initPostcard.event,
                    price = initPostcard.price,
                    ownerId = PsStubRose.PS_ROSE1.ownerId,
                )
            )
        },
        invokeUpdatePostcard = {
            DbPostcardResponseOk(
                data = Postcard(
                    id = initPostcard.id,
                    title = initPostcard.title,
                    author = initPostcard.author,
                    event = initPostcard.event,
                    price = 101,
                    ownerId = PsStubRose.PS_ROSE1.ownerId,
                )
            )
        },
        invokeDeletePostcard = {
            DbPostcardResponseOk(
                data = Postcard(
                    id = initPostcard.id,
                    title = initPostcard.title,
                    author = initPostcard.author,
                    event = initPostcard.event,
                    price = initPostcard.price,
                    ownerId = PsStubRose.PS_ROSE1.ownerId,
                )
            )
        },
        invokeSearchPostcard = {
            DbPostcardsResponseOk(
                data = listOf(initPostcard),
            )
        }
    )
    private val settings = PsCorSettings(
        repoTest = repo
    )
    private val processor = PsProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = initRepoContext(PsCommand.CREATE)
        processor.exec(ctx)
        assertEquals(FINISHING, ctx.state)
        assertNotEquals(PostcardId.NONE, ctx.postcardResponse.id)
        assertEquals("Hello World", ctx.postcardResponse.title)
        assertEquals(setOf("Годовщина"), ctx.postcardResponse.event)
        assertEquals(setOf("Васнецова"), ctx.postcardResponse.author)
        assertEquals(100, ctx.postcardResponse.price)
    }

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = initRepoContext(PsCommand.READ)
        processor.exec(ctx)
        assertEquals(FINISHING, ctx.state)
        assertEquals(initPostcard.id, ctx.postcardResponse.id)
        assertEquals(initPostcard.title, ctx.postcardResponse.title)
        assertEquals(initPostcard.event, ctx.postcardResponse.event)
        assertEquals(initPostcard.author, ctx.postcardResponse.author)
        assertEquals(initPostcard.price, ctx.postcardResponse.price)
    }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val postcardToUpdate = Postcard(
            id = PostcardId("123-456-7890"),
            title = "Hello World",
            author = setOf("Васнецова"),
            event = setOf("Годовщина"),
            price = 101,
        )
        val ctx = initRepoContext(PsCommand.UPDATE, postcardToUpdate)
        processor.exec(ctx)
        assertEquals(FINISHING, ctx.state)
        assertEquals(postcardToUpdate.id, ctx.postcardResponse.id)
        assertEquals(postcardToUpdate.title, ctx.postcardResponse.title)
        assertEquals(postcardToUpdate.event, ctx.postcardResponse.event)
        assertEquals(postcardToUpdate.author, ctx.postcardResponse.author)
        assertEquals(postcardToUpdate.price, ctx.postcardResponse.price)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(PsCommand.UPDATE)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val postcardToDelete = Postcard(
            id = initPostcard.id,
        )
        val ctx = initRepoContext(PsCommand.DELETE, postcardToDelete)
        processor.exec(ctx)
        assertEquals(FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initPostcard.id, ctx.repoPostcardDone.id)
        assertEquals(initPostcard.title, ctx.repoPostcardDone.title)
        assertEquals(initPostcard.event, ctx.repoPostcardDone.event)
        assertEquals(initPostcard.author, ctx.repoPostcardDone.author)
        assertEquals(initPostcard.price, ctx.repoPostcardDone.price)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(PsCommand.DELETE)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = initRepoContext(PsCommand.SEARCH, filter = PostcardFilter(searchString = "Hello"))
        processor.exec(ctx)
        assertEquals(FINISHING, ctx.state)
        assertEquals(1, ctx.postcardsResponse.size)
    }
}