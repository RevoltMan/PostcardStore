package ru.otus.otuskotlin.postcardshop.ktor.repo

import org.junit.experimental.runners.Enclosed
import org.junit.jupiter.api.AfterEach
import org.junit.runner.RunWith
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugMode
import ru.otus.otuskotlin.postcardshop.app.common.PostcardAppSettings
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import ru.otus.otuskotlin.postcardshop.ktor.PostcardAppSettingsImpl
import ru.otus.otuskotlin.postcardshop.repo.pg.RepoPostcardSql
import ru.otus.otuskotlin.postcardshop.repo.pg.getDbTestParam
import kotlin.test.BeforeTest

@RunWith(Enclosed::class)
open class PostcardRepoPgTest : PostcardRepoBaseTest()  {
    override val workMode = PostcardRequestDebugMode.TEST
    @AfterEach
    fun afterTest() {
        val pgRepo = cleanRepo.repo as RepoPostcardSql
        pgRepo.clearTest()
    }

    private fun postcardAppSettings(repo: RepoPostcard) = PostcardAppSettingsImpl(
        corSettings = PsCorSettings(
            repoTest = repo,
            repoProd = repo,
        )
    )

    override val appSettingsCreate: PostcardAppSettings by lazy {
        postcardAppSettings(
            repo = getDbTestParam(
            )
        )
    }
    override val appSettingsRead: PostcardAppSettings by lazy {
        postcardAppSettings(
            repo = getDbTestParam(
                initObjects = listOf(initPostcard),
            )
        )
    }
    override val appSettingsUpdate: PostcardAppSettings by lazy {
        postcardAppSettings(
            repo = getDbTestParam(
                initObjects = listOf(initPostcard),
            )
        )
    }
    override val appSettingsDelete: PostcardAppSettings by lazy {
        postcardAppSettings(
            repo = getDbTestParam(
                initObjects = listOf(initPostcard),
                randomUuid = { uuidNew },
            )
        )
    }
    override val appSettingsSearch: PostcardAppSettings by lazy {
        postcardAppSettings(
            repo = getDbTestParam(
                initObjects = listOf(initPostcard),
                randomUuid = { uuidNew },
            )
        )
    }

    private val cleanRepo = getDbTestParam()

    @BeforeTest
    fun beforeTest() {
        val pgRepo = cleanRepo.repo as RepoPostcardSql
        pgRepo.clearTest()
    }
}