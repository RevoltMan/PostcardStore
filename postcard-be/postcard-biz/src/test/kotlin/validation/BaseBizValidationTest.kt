package ru.otus.otuskotlin.postcardshop.biz.validation

import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub
import ru.otus.otuskotlin.postcardshop.repo.common.PostcardRepoInitialized
import ru.otus.otuskotlin.postcardshop.repo.inmemory.PostcardRepoInMemory

abstract class BaseBizValidationTest {
    protected abstract val command: PsCommand
    private val repo = PostcardRepoInitialized(
        repo = PostcardRepoInMemory(),
        initObjects = listOf(
            PostcardStub.get(),
        ),
    )
    private val settings by lazy { PsCorSettings(repoTest = repo) }
    protected val processor by lazy { PsProcessor(settings) }
}