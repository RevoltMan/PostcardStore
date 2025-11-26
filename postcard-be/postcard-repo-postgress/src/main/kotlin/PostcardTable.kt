package ru.otus.otuskotlin.postcardshop.repo.pg

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId

class PostcardTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE)
    val author = text(SqlFields.AUTHOR)
    val event = text(SqlFields.EVENT)
    val price = integer(SqlFields.PRICE)
    val owner = text(SqlFields.OWNER_ID)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = Postcard(
        id = PostcardId(res[id]),
        title = res[title],
        author = res[author].asSet(),
        event = res[event].asSet(),
        price = res[price],
        ownerId = PsUserId(res[owner]),
    )

    fun UpdateBuilder<*>.to(postcard: Postcard, randomUuid: () -> String) {
        this[id] = postcard.id.takeIf { it != PostcardId.NONE }?.asString() ?: randomUuid()
        this[title] = postcard.title
        this[author] = postcard.author.asString()
        this[event] = postcard.event.asString()
        this[price] = postcard.price
        this[owner] = postcard.ownerId.asString()
    }

    private fun Set<String>.asString(): String =
        this.joinToString(prefix = "", separator = "!!", postfix = "")

    private fun String.asSet(): Set<String> =
        this.split("!!").toSet()
}