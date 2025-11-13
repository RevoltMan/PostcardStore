package biz.repo

import biz.exception.PostcardDbNotConfiguredException
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.cor.CorChainDsl
import ru.otus.otuskotlin.postcardshop.common.cor.worker
import ru.otus.otuskotlin.postcardshop.common.helpers.errorSystem
import ru.otus.otuskotlin.postcardshop.common.helpers.fail
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardFilterRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardIdRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseErr
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseErrWithData
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseErr
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard

fun CorChainDsl<PsContext>.initRepo(title: String) = worker {
    this.title = title
    description = "Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы"
    handle {
        repoPostcard = when {
            workMode == PsWorkMode.TEST -> corSettings.repoTest
            workMode == PsWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != PsWorkMode.STUB && repoPostcard == RepoPostcard.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                th = PostcardDbNotConfiguredException(workMode)
            )
        )
    }
}

fun CorChainDsl<PsContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != PsWorkMode.STUB }
    handle {
        postcardResponse = repoPostcardDone
        postcardsResponse = repoPostcardsDone
        state = when (val st = state) {
            PsState.RUNNING -> PsState.FINISHING
            else -> st
        }
    }
}

fun CorChainDsl<PsContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == PsState.RUNNING }
    handle { }
}

fun CorChainDsl<PsContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление открытки в БД"
    on { state == PsState.RUNNING }
    handle {
        val request = DbPostcardRequest(postcardRequest)
        when(val result = repoPostcard.createPostcard(request)) {
            is DbPostcardResponseOk -> repoPostcardDone = result.data
            is DbPostcardResponseErr -> fail(result.errors)
            is DbPostcardResponseErrWithData -> fail(result.errors)
        }
    }
}

fun CorChainDsl<PsContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение открытки из БД"
    on { state == PsState.RUNNING }
    handle {
        val request = DbPostcardIdRequest(postcardRequest)
        when(val result = repoPostcard.readPostcard(request)) {
            is DbPostcardResponseOk -> repoPostcardRead = result.data
            is DbPostcardResponseErr -> fail(result.errors)
            is DbPostcardResponseErrWithData -> {
                fail(result.errors)
                repoPostcardRead = result.data
            }
        }
    }
}

fun CorChainDsl<PsContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, и данные, полученные от пользователя"
    on { state == PsState.RUNNING }
    handle { }
}

fun CorChainDsl<PsContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == PsState.RUNNING }
    handle {
        val request = DbPostcardRequest(postcardRequest)
        when(val result = repoPostcard.updatePostcard(request)) {
            is DbPostcardResponseOk -> repoPostcardDone = result.data
            is DbPostcardResponseErr -> fail(result.errors)
            is DbPostcardResponseErrWithData -> {
                fail(result.errors)
                repoPostcardDone = result.data
            }
        }
    }
}

fun CorChainDsl<PsContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Готовим данные к удалению из БД"
    on { state == PsState.RUNNING }
    handle { }
}

fun CorChainDsl<PsContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление открытки из БД по ID"
    on { state == PsState.RUNNING }
    handle {
        val request = DbPostcardIdRequest(postcardRequest)
        val result = repoPostcard.deletePostcard(request)
        when(result) {
            is DbPostcardResponseOk -> repoPostcardDone = result.data
            is DbPostcardResponseErr -> {
                fail(result.errors)
                repoPostcardDone = postcardRequest
            }
            is DbPostcardResponseErrWithData -> {
                fail(result.errors)
                repoPostcardDone = result.data
            }
        }
    }
}

fun CorChainDsl<PsContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск открыток в БД по фильтру"
    on { state == PsState.RUNNING }
    handle {
        with(postcardFilterRequest) {
            val request = DbPostcardFilterRequest(
                title = searchString,
                ownerId = ownerId,
                event = event,
                author = author,
                price = price,
            )
            when(val result = repoPostcard.searchPostcard(request)) {
                is DbPostcardsResponseOk -> repoPostcardsDone = result.data.toMutableList()
                is DbPostcardsResponseErr -> fail(result.errors)
            }
        }
    }
}