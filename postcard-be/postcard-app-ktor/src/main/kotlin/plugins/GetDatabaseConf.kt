package ru.otus.otuskotlin.postcardshop.ktor.plugins

import io.ktor.server.application.Application
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import ru.otus.otuskotlin.postcardshop.ktor.config.ConfigPaths
import ru.otus.otuskotlin.postcardshop.repo.inmemory.PostcardRepoInMemory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Application.getDatabaseConf(type: PostcardDbType): RepoPostcard {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    println ("путь $dbSettingPath")
    return when (val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase() ?: "inmemory") {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
//        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath has value of '$dbSetting', but it must be set in application.yml to one of: " +
                    "'inmemory', 'postgres'"
        )
    }
}

//fun Application.initPostgres(): RepoPostcard {
//    val config = PostgresConfig(environment.config)
//    return RepoPostcardSql(
//        properties = SqlProperties(
//            host = config.host,
//            port = config.port,
//            user = config.user,
//            password = config.password,
//            schema = config.schema,
//            database = config.database,
//        ),
//    )
//}

enum class PostcardDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.initInMemory(): RepoPostcard {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return PostcardRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}
