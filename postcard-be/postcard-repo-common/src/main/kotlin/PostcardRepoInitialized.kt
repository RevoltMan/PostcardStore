package ru.otus.otuskotlin.postcardshop.repo.common

import ru.otus.otuskotlin.postcardshop.common.models.Postcard

class PostcardRepoInitialized(
    val repo: RepoPostcardInitializable,
    initObjects: Collection<Postcard> = emptyList(),
) : RepoPostcardInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Postcard> = save(initObjects).toList()
}