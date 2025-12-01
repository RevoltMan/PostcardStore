package ru.otus.otuskotlin.postcardshop.repo.pg

import com.benasher44.uuid.uuid4
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.repo.common.PostcardRepoInitialized

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "Prostor",
    val database: String = "postgres",
    val schema: String = "public",
    val table: String = "postcard",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}

    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "Prostor"
    private const val PORT = 5432


fun getDbTestParam(
    initObjects: Collection<Postcard> = emptyList(),
    randomUuid: () -> String = { uuid4().toString() },
) = PostcardRepoInitialized(
    repo = RepoPostcardSql(
        SqlProperties(
            host = HOST,
            user = USER,
            password = PASS,
            port = PORT,
        ),
        randomUuid = { "test-$randomUuid" }
    ),
    initObjects = initObjects,
)