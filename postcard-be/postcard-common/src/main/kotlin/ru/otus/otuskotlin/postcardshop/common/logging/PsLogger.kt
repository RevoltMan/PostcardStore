package ru.otus.otuskotlin.postcardshop.common.logging


import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal PsLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun psLoggerLogback(logger: Logger): PsLogWrapper = PsLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun psLoggerLogback(clazz: KClass<*>): PsLogWrapper = psLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun psLoggerLogback(loggerId: String): PsLogWrapper = psLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
