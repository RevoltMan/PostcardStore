package ru.otus.otuskotlin.postcardshop.ktor.base

import io.ktor.server.config.ApplicationConfig
import ru.otus.otuskotlin.postcardshop.ktor.config.ConfigPaths

data class PostgresConfig(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "Prostor",
    val database: String = "postgres",
    val schema: String = "public",
) {
    constructor(config: ApplicationConfig): this(
        host = config.propertyOrNull("$PATH.host")?.getString() ?: "localhost",
        port = config.propertyOrNull("$PATH.port")?.getString()?.toIntOrNull() ?: 5432,
        user = config.propertyOrNull("$PATH.user")?.getString() ?: "postgres",
        password = config.property("$PATH.password").getString(),
        database = config.propertyOrNull("$PATH.database")?.getString() ?: "postgres",
        schema = config.propertyOrNull("$PATH.schema")?.getString() ?: "public",
    )

    companion object {
        const val PATH = "${ConfigPaths.repository}.psql"
    }
}