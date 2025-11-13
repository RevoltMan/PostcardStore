package ru.otus.otuskotlin.postcardshop.repo.tests

import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId

internal interface InitObjects<T> {
    val initObjects: List<T>
}

abstract class BaseInitPostcards(private val op: String): InitObjects<Postcard> {
    fun createInitTestModel(
        suffix: String,
        ownerId: PsUserId = PsUserId("owner"),

        ) = Postcard(
        id = PostcardId("postcard-repo-$op-$suffix"),
        title = "$suffix stub",
        author = setOf("$suffix stub author"),
        event = setOf("$suffix stub event"),
        price = 135,
        ownerId = ownerId,
     )
}