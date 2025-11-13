package ru.otus.otuskotlin.postcardshop.ktor.repo

import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugMode
import ru.otus.otuskotlin.postcardshop.app.common.PostcardAppSettings
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import ru.otus.otuskotlin.postcardshop.ktor.PostcardAppSettingsImpl
import ru.otus.otuskotlin.postcardshop.repo.common.PostcardRepoInitialized
import ru.otus.otuskotlin.postcardshop.repo.inmemory.PostcardRepoInMemory

class PostcardRepoInmemoryTest : PostcardRepoBaseTest() {
    override val workMode: PostcardRequestDebugMode = PostcardRequestDebugMode.TEST
    private fun mkAppSettings(repo: RepoPostcard) = PostcardAppSettingsImpl(
        corSettings = PsCorSettings(
            repoTest = repo
        )
    )
    override val appSettingsCreate: PostcardAppSettings = mkAppSettings(
        repo = PostcardRepoInitialized(PostcardRepoInMemory(randomUuid = { uuidNew }))
    )
    override val appSettingsRead: PostcardAppSettings = mkAppSettings(
        repo = PostcardRepoInitialized(
            PostcardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initPostcard),
        )
    )
    override val appSettingsUpdate: PostcardAppSettings = mkAppSettings(
        repo = PostcardRepoInitialized(
            PostcardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initPostcard),
        )
    )
    override val appSettingsDelete: PostcardAppSettings = mkAppSettings(
        repo = PostcardRepoInitialized(
            PostcardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initPostcard),
        )
    )
    override val appSettingsSearch: PostcardAppSettings = mkAppSettings(
        repo = PostcardRepoInitialized(
            PostcardRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initPostcard),
        )
    )
}