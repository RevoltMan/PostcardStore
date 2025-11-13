package ru.otus.otuskotlin.postcardshop.common.repo.extentions

import ru.otus.otuskotlin.postcardshop.common.models.PostcardId

class RepoPostcardException(
    @Suppress("unused")
    val postcardId: PostcardId,
    msg: String,
): RepoException(msg)