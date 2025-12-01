package ru.otus.otuskotlin.postcardshop.biz

import biz.repo.initRepo
import biz.repo.prepareResult
import biz.repo.repoCreate
import biz.repo.repoDelete
import biz.repo.repoPrepareCreate
import biz.repo.repoPrepareDelete
import biz.repo.repoPrepareUpdate
import biz.repo.repoRead
import biz.repo.repoSearch
import biz.repo.repoUpdate
import biz.validateAuthorsNotEmpty
import biz.validateEventNotEmpty
import biz.validateIdNotEmpty
import biz.validateIdProperFormat
import biz.validatePriceGreaterZero
import biz.validateSearchStringLength
import biz.validateTitleNotEmpty
import biz.validation
import ru.otus.otuskotlin.postcardshop.biz.general.operation
import ru.otus.otuskotlin.postcardshop.biz.general.stubs
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings.Companion.NONE
import ru.otus.otuskotlin.postcardshop.common.cor.chain
import ru.otus.otuskotlin.postcardshop.common.cor.rootChain
import ru.otus.otuskotlin.postcardshop.common.cor.worker
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsRequestId
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.models.PsState.RUNNING

class PsProcessor(val corSettings: PsCorSettings = NONE) {

    suspend fun exec(ctx: PsContext) =
        businessChain.exec( ctx.also {it.corSettings = corSettings})

    private val businessChain = rootChain<PsContext> {
        worker {
            title = "Инициализация статуса"
            on { state == PsState.NONE }
            handle {
                state = RUNNING
            }
        }
        initRepo("Инициализация репозитория")

        operation("Создание открытку", PsCommand.CREATE) {
            stubs("Обработка заглушек") {
                stubCreateContext("Успех", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadAuthor("Имитация ошибки валидации автора")
                stubValidationBadPrice("Имитация ошибки валидации цены")
            }
            validation {
                worker("Очистка id запроса") { requestId = PsRequestId.NONE }
                worker("Очистка id открытки") { postcardRequest.id = PostcardId.NONE }
                worker("Удаление пробелов у заголовка") { postcardRequest.title = postcardRequest.title.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateAuthorsNotEmpty("Проверка, что список авторов не пуст")
                validateEventNotEmpty("Проверка, что событие не пусто")
                validatePriceGreaterZero("Проверка, что сумма больше нуля")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание открытки в БД")
            }
            prepareResult("Подготовка ответа")
        }

        operation("Чтение открытки", PsCommand.READ) {
            stubs("Обработка заглушек") {
                stubReadContext("Успех", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationNotFound("Имитация что запись не найдена")
            }

            validation {
                worker("Удаление пробелов в id") { postcardRequest.id = PostcardId(postcardRequest.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
            }

            chain {
                title = "Логика чтения"
                repoRead("Чтение открытки из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == RUNNING }
                    handle { repoPostcardDone = repoPostcardRead }
                }
            }
            prepareResult("Подготовка ответа")
        }

        operation("Изменить открытку", PsCommand.UPDATE) {
            stubs("Обработка заглушек") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationNotFound("Имитация что запись не найдена")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadAuthor("Имитация ошибки валидации автора")
                stubValidationBadPrice("Имитация ошибки валидации цены")
            }

            validation {
                worker("Удаление пробелов в id") { postcardRequest.id = PostcardId(postcardRequest.id.asString().trim()) }
                worker("Очистка заголовка") { postcardRequest.title = postcardRequest.title.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateAuthorsNotEmpty("Проверка, что список авторов не пуст")
                validateEventNotEmpty("Проверка, что событие не пусто")
                validatePriceGreaterZero("Проверка, что сумма больше нуля")
            }

            chain {
                title = "Логика сохранения"
                repoRead("Чтение открытки из БД")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление открытки в БД")
            }
            prepareResult("Подготовка ответа")
        }

        operation("Удалить открытку", PsCommand.DELETE) {
            stubs("Обработка заглушек") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationNotFound("Имитация что запись не найдена")
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationCanNotDelete("Имитация ошибки при удалении открытки")
            }

            validation {
                worker("Удаление пробелов в id") { postcardRequest.id = PostcardId(postcardRequest.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
            }

            chain {
                title = "Логика удаления"
                repoRead("Чтение открытки из БД")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление открытки из БД")
            }
            prepareResult("Подготовка ответа")
        }

        operation("Поиск открытки", PsCommand.SEARCH) {
            stubs("Обработка заглушек") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationNotFound("Имитация что запись не найдена")
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadSearchString("Имитация ошибки плохой строки поиска")
            }
            validation {
                validateSearchStringLength("Валидация длины строки поиска в фильтре")
            }
            repoSearch("Поиск открытки в БД по фильтру")
            prepareResult("Подготовка ответа")
        }

    }.build()
}