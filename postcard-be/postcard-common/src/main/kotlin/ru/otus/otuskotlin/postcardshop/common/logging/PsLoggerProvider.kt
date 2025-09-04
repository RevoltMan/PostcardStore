package ru.otus.otuskotlin.postcardshop.common.logging

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class PsLoggerProvider (
    private val provider: (String) -> PsLogWrapper = { PsLogWrapper.DEFAULT }
) {
    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(loggerId: String): PsLogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(clazz: KClass<*>): PsLogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(function: KFunction<*>): PsLogWrapper = provider(function.name)
}