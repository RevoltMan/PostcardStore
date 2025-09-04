package ru.otus.otuskotlin.postcardshop.common.logging

import kotlinx.datetime.Clock
import kotlin.time.measureTimedValue

interface PsLogWrapper: AutoCloseable {
    val loggerId: String

    fun log(
        msg: String = "",
        level: LogLevel = LogLevel.TRACE,
        marker: String = "DEV",
        ex: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null,
    )

    fun error(
        msg: String = "",
        marker: String = "DEV",
        ex: Throwable? = null,
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, LogLevel.ERROR, marker, ex, data, objs)

    fun warn(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, LogLevel.WARN, marker, null, data, objs)

    fun info(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, LogLevel.INFO, marker, null, data, objs)

    fun debug(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objs: Map<String, Any>? = null,
    ) = log(msg, LogLevel.DEBUG, marker, null, data, objs)

    /**
     * Функция обертка для выполнения прикладного кода с логированием перед выполнением и после
     */
//    @OptIn(ExperimentalTime::class)
    suspend fun <T> doWithLogging(
        id: String = "",
        level: LogLevel = LogLevel.INFO,
        block: suspend () -> T,
    ): T = try {
        log("Started $loggerId $id", level)
        val (res, diffTime) = measureTimedValue { block() }

        log(
            msg = "Finished $loggerId $id",
            level = level,
            objs = mapOf("metricHandleTime" to diffTime.toIsoString())
        )
        res
    } catch (ex: Throwable) {
        log(
            msg = "Failed $loggerId $id",
            level = LogLevel.ERROR,
            ex = ex
        )
        throw ex
    }

    /**
     * Функция обертка для выполнения прикладного кода с логированием ошибки
     */
    suspend fun <T> doWithErrorLogging(
        id: String = "",
        throwRequired: Boolean = true,
        block: suspend () -> T,
    ): T? = try {
        val result = block()
        result
    } catch (ex: Throwable) {
        log(
            msg = "Failed $loggerId $id",
            level = LogLevel.ERROR,
            ex = ex
        )
        if (throwRequired) throw ex else null
    }

    override fun close() {}

    companion object {
        val DEFAULT = object: PsLogWrapper {
            override val loggerId: String = "NONE"

            override fun log(
                msg: String,
                level: LogLevel,
                marker: String,
                ex: Throwable?,
                data: Any?,
                objs: Map<String, Any>?,
            ) {
                val markerString = marker
                    .takeIf { it.isNotBlank() }
                    ?.let { " ($it)" }
                val args = listOfNotNull(
                    "${Clock.System.now()} [${level.name}]$markerString: $msg",
                    ex?.let { "${it.message ?: "Unknown reason"}:\n${it.stackTraceToString()}" },
                    data.toString(),
                    objs.toString(),
                )
                println(args.joinToString("\n"))
            }
        }
    }
}