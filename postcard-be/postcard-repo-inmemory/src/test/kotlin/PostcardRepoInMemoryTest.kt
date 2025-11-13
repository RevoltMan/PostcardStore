package ru.otus.otuskotlin.postcardshop.repo.inmemory

import com.benasher44.uuid.uuid4
import ru.otus.otuskotlin.postcardshop.repo.common.PostcardRepoInitialized
import ru.otus.otuskotlin.postcardshop.repo.tests.*

class PostcardRepoInMemoryCreateTest : RepoPostcardCreateTest() {
    override val repo = PostcardRepoInitialized(
        PostcardRepoInMemory(randomUuid = { uuid4().toString() }),
        initObjects = initObjects,
    )
}

class PostcardRepoInMemoryDeleteTest : RepoPostcardDeleteTest() {
    override val repo = PostcardRepoInitialized(
        PostcardRepoInMemory(),
        initObjects = initObjects,
    )
}

class PostcardRepoInMemoryReadTest : RepoPostcardReadTest() {
    override val repo = PostcardRepoInitialized(
        PostcardRepoInMemory(),
        initObjects = initObjects,
    )
}

class PostcardRepoInMemorySearchTest : RepoPostcardSearchTest() {
    override val repo = PostcardRepoInitialized(
        PostcardRepoInMemory(),
        initObjects = initObjects,
    )
}

class PostcardRepoInMemoryUpdateTest : RepoPostcardUpdateTest() {
    override val repo = PostcardRepoInitialized(
        PostcardRepoInMemory(randomUuid = { uuid4().toString() }),
        initObjects = initObjects,
    )
}