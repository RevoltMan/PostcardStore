package ru.otus.otuskotlin.postcardshop.repo.pg

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.updateReturning
import ru.otus.otuskotlin.postcardshop.common.helpers.asPostcardError
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardFilterRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardIdRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponse
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseErr
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponse
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseErr
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import ru.otus.otuskotlin.postcardshop.common.repo.errorEmptyId
import ru.otus.otuskotlin.postcardshop.common.repo.errorNotFound
import ru.otus.otuskotlin.postcardshop.repo.common.RepoPostcardInitializable

class RepoPostcardSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : RepoPostcard, RepoPostcardInitializable {
    private val postcardTable = PostcardTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    @Suppress("unused")
    fun clear(): Unit = transaction(conn) {
        postcardTable.deleteAll()
    }

    fun clearTest(): Unit = transaction(conn) {
        postcardTable.deleteWhere{
            postcardTable.id like "test%"
        }
    }

    private fun saveObj(postcard: Postcard): Postcard = transaction(conn) {
        try {
            val res = postcardTable
                .insert {
                    it.to(postcard, randomUuid)
                }
                .resultedValues
                ?.map { postcardTable.from(it) }
            res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")

        } catch (ex: Exception) {
            throw ex
        }
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> DbPostcardResponse): DbPostcardResponse =
        transactionWrapper(block) { DbPostcardResponseErr(it.asPostcardError()) }

    override fun save(postcards: Collection<Postcard>): Collection<Postcard> = postcards.map { saveObj(it) }
    override suspend fun createPostcard(rq: DbPostcardRequest): DbPostcardResponse = transactionWrapper {
        DbPostcardResponseOk(saveObj(rq.postcard))
    }

    private fun read(id: PostcardId): DbPostcardResponse {
        val res = postcardTable.selectAll().where {
            postcardTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbPostcardResponseOk(postcardTable.from(res))
    }

    override suspend fun readPostcard(rq: DbPostcardIdRequest): DbPostcardResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: PostcardId,
        block: (Postcard) -> DbPostcardResponse
    ): DbPostcardResponse =
        transactionWrapper {
            if (id == PostcardId.NONE) return@transactionWrapper errorEmptyId

            val current = postcardTable.selectAll().where { postcardTable.id eq id.asString() }
                .singleOrNull()
                ?.let { postcardTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                else -> block(current)
            }
        }


    override suspend fun updatePostcard(rq: DbPostcardRequest): DbPostcardResponse = update(rq.postcard.id) {
        postcardTable.updateReturning(where = { postcardTable.id eq rq.postcard.id.asString() }) {
            it.to(rq.postcard.copy(), randomUuid)
        }.singleOrNull()
            ?.let { DbPostcardResponseOk(postcardTable.from(it)) }
            ?: errorNotFound(rq.postcard.id)
    }

    override suspend fun deletePostcard(rq: DbPostcardIdRequest): DbPostcardResponse = update(rq.id) {
        postcardTable.deleteWhere { id eq rq.id.asString() }
        DbPostcardResponseOk(it)
    }

    override suspend fun searchPostcard(rq: DbPostcardFilterRequest): DbPostcardsResponse =
        transactionWrapper({
            val res = postcardTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != PsUserId.NONE) {
                        add(postcardTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.title.isNotBlank()) {
                        add(
                            (postcardTable.title like "%${rq.title}%")
                        )
                    }
                    if (rq.author.isNotBlank()) {
                        add(
                            (postcardTable.author like "%${rq.title}%")
                        )
                    }
                    if (rq.event.isNotBlank()) {
                        add(
                            (postcardTable.event like "%${rq.title}%")
                        )
                    }
                    if (rq.price != null && rq.price!! > 0) {
                        add(
                            (postcardTable.price eq rq.price!!)
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbPostcardsResponseOk(data = res.map { postcardTable.from(it) })
        }, {
            DbPostcardsResponseErr(it.asPostcardError())
        })
}