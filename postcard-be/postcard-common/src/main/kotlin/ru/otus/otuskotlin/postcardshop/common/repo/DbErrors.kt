package ru.otus.otuskotlin.postcardshop.common.repo

import ru.otus.otuskotlin.postcardshop.common.helpers.errorSystem
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsError
import ru.otus.otuskotlin.postcardshop.common.repo.extentions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: PostcardId) = DbPostcardResponseErr(
    PsError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbPostcardResponseErr(
    PsError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorDb(ex: RepoException) = DbPostcardResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        th = ex
    )
)
