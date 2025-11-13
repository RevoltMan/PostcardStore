package ru.otus.otuskotlin.postcardshop.biz.repo

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.biz.stub.initPostcard
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardFilter
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.errorNotFound
import ru.otus.otuskotlin.postcardshop.repo.tests.PostcardRepositoryMock

val initPostcard = initPostcard()

private val repo = PostcardRepositoryMock(
    invokeReadPostcard = {
        if (it.id == initPostcard.id) {
            DbPostcardResponseOk(
                data = initPostcard,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = PsCorSettings(repoTest = repo)
private val processor = PsProcessor(settings)

fun repoNotFoundTest(command: PsCommand) = runTest {
    val ctx = PsContext(
        command = command,
        state = PsState.NONE,
        workMode = PsWorkMode.TEST,
        postcardRequest = Postcard(
            id = PostcardId("12345"),
            title = "xyz",
            author = setOf("Василий"),
            event = setOf("Застолье"),
            price = 54321,
        ),
    )
    processor.exec(ctx)
    assertEquals(PsState.FAILING, ctx.state)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}

fun initRepoContext(command: PsCommand, postcard: Postcard = initPostcard, filter: PostcardFilter = PostcardFilter()): PsContext =
    PsContext (
        command = command,
        state = PsState.NONE,
        workMode = PsWorkMode.TEST,
        postcardRequest = postcard,
        postcardFilterRequest = filter,
    )