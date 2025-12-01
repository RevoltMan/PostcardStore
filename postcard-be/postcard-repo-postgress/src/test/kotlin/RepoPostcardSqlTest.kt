package ru.otus.otuskotlin.postcardshop.repo.pg

import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import ru.otus.otuskotlin.postcardshop.repo.common.PostcardRepoInitialized
import ru.otus.otuskotlin.postcardshop.repo.common.RepoPostcardInitializable
import ru.otus.otuskotlin.postcardshop.repo.tests.RepoPostcardCreateTest
import ru.otus.otuskotlin.postcardshop.repo.tests.RepoPostcardDeleteTest
import ru.otus.otuskotlin.postcardshop.repo.tests.RepoPostcardReadTest
import ru.otus.otuskotlin.postcardshop.repo.tests.RepoPostcardSearchTest
import ru.otus.otuskotlin.postcardshop.repo.tests.RepoPostcardUpdateTest
import kotlin.test.AfterTest

private fun RepoPostcardInitializable.clear() {
    val pgRepo = (this as PostcardRepoInitialized).repo as RepoPostcardSql
    pgRepo.clearTest()
}

@RunWith(Enclosed::class)
class RepoPostcardSqlTest {

    class RepoPostcardSqlCreateTest : RepoPostcardCreateTest() {
        override val repo = getDbTestParam(
            initObjects,
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoPostcardSqlReadTest : RepoPostcardReadTest() {
        override val repo = getDbTestParam(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoPostcardSqlUpdateTest : RepoPostcardUpdateTest() {
        override val repo = getDbTestParam(
            initObjects,
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoPostcardSqlDeleteTest : RepoPostcardDeleteTest() {
        override val repo = getDbTestParam(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoPostcardSqlSearchTest : RepoPostcardSearchTest() {
        override val repo = getDbTestParam(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }
}