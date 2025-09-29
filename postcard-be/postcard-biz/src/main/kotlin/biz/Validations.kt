package biz

import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.cor.CorChainDsl
import ru.otus.otuskotlin.postcardshop.common.cor.chain
import ru.otus.otuskotlin.postcardshop.common.cor.worker
import ru.otus.otuskotlin.postcardshop.common.helpers.errorValidation
import ru.otus.otuskotlin.postcardshop.common.helpers.fail
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsState.RUNNING

fun CorChainDsl<PsContext>.validation(block: CorChainDsl<PsContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == RUNNING }
}

fun CorChainDsl<PsContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { postcardRequest.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                validationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun CorChainDsl<PsContext>.validateAuthorsNotEmpty(title: String) = worker {
    this.title = title
    on { isEmptySetValue(postcardRequest.author) }
    handle {
        fail(
            errorValidation(
                field = "author",
                validationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun CorChainDsl<PsContext>.validateEventNotEmpty(title: String) = worker {
    this.title = title
    on { isEmptySetValue(postcardRequest.event) }
    handle {
        fail(
            errorValidation(
                field = "event",
                validationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

private fun isEmptySetValue(values: Set<String>): Boolean {
    if (values.isEmpty()) {
        return true
    }
    return if (values.firstOrNull { it.trim().isEmpty() } == null) false else true
}

fun CorChainDsl<PsContext>.validatePriceGreaterZero(title: String) = worker {
    this.title = title
    on { postcardRequest.price <=0 }
    handle {
        fail(
            errorValidation(
                field = "price",
                validationCode = "greater",
                description = "field must be greater zero"
            )
        )
    }
}

fun CorChainDsl<PsContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { postcardRequest.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                validationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

fun CorChainDsl<PsContext>.validateIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { postcardRequest.id != PostcardId.NONE && !postcardRequest.id.asString().matches(regExp) }
    handle {
        val encodedId = postcardRequest.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                validationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}

fun CorChainDsl<PsContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    this.description = "Валидация длины строки поиска в поисковых фильтрах."
    // Допустимые значения:
    //    - null - не выполняем поиск по строке
    //    - 3-100 - допустимая длина
    on { true }
    worker("Обрезка пустых символов") { postcardFilterRequest.searchString = postcardFilterRequest.searchString.trim() }
    worker {
        this.title = "Проверка кейса длины на 0-2 символа"
        this.description = this.title
        on { postcardFilterRequest.searchString.length in (1..2) }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    validationCode = "tooShort",
                    description = "Search string must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка кейса длины на более 100 символов"
        this.description = this.title
        on { state == RUNNING && postcardFilterRequest.searchString.length > 100 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    validationCode = "tooLong",
                    description = "Search string must be no more than 100 symbols long"
                )
            )
        }
    }
}