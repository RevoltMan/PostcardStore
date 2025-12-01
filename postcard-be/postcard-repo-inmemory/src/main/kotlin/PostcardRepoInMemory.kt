package ru.otus.otuskotlin.postcardshop.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardFilterRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardIdRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardRequest
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponse
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponse
import ru.otus.otuskotlin.postcardshop.common.repo.DbPostcardsResponseOk
import ru.otus.otuskotlin.postcardshop.common.repo.PostcardRepoBase
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard
import ru.otus.otuskotlin.postcardshop.common.repo.errorEmptyId
import ru.otus.otuskotlin.postcardshop.common.repo.errorNotFound
import ru.otus.otuskotlin.postcardshop.repo.common.RepoPostcardInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class PostcardRepoInMemory (
    ttl: Duration = 10.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : PostcardRepoBase(), RepoPostcard, RepoPostcardInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, PostcardEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(postcards: Collection<Postcard>) = postcards.map { postcard ->
        val entity = PostcardEntity(postcard)
        require(entity.id != null)
        cache.put(entity.id!!, entity)
        postcard
    }

    override suspend fun createPostcard(rq: DbPostcardRequest): DbPostcardResponse = tryPostcardMethod {
        val key = randomUuid()
        val postcard = rq.postcard.copy(id = PostcardId(key))
        val entity = PostcardEntity(postcard)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbPostcardResponseOk(postcard)
    }

    override suspend fun readPostcard(rq: DbPostcardIdRequest): DbPostcardResponse = tryPostcardMethod {
        val key = rq.id.takeIf { it != PostcardId.NONE }?.asString() ?: return@tryPostcardMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbPostcardResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updatePostcard(rq: DbPostcardRequest): DbPostcardResponse = tryPostcardMethod {
        val rqPostcard = rq.postcard
        val id = rqPostcard.id.takeIf { it != PostcardId.NONE } ?: return@tryPostcardMethod errorEmptyId
        val key = id.asString()
        mutex.withLock {
            val oldPostcard = cache.get(key)?.toInternal()
            when {
                oldPostcard == null -> errorNotFound(id)
                else -> {
                    val newPostcard = rqPostcard.copy()
                    val entity = PostcardEntity(newPostcard)
                    cache.put(key, entity)
                    DbPostcardResponseOk(newPostcard)
                }
            }
        }
    }


    override suspend fun deletePostcard(rq: DbPostcardIdRequest): DbPostcardResponse = tryPostcardMethod {
        val id = rq.id.takeIf { it != PostcardId.NONE } ?: return@tryPostcardMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldPostcard = cache.get(key)?.toInternal()
            when {
                oldPostcard == null -> errorNotFound(id)
                else -> {
                    cache.invalidate(key)
                    DbPostcardResponseOk(oldPostcard)
                }
            }
        }
    }

    /**
     * Поиск открытки по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchPostcard(rq: DbPostcardFilterRequest): DbPostcardsResponse = tryPostcardsMethod {
        val result: List<Postcard> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != PsUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.price.takeIf { it != null && it > 0 }?.let {
                    it == entry.value.price
                } ?: true
            }
            .filter { entry ->
                rq.title.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .filter { entry ->
                rq.author.takeIf { it.isNotBlank() }?.let {
                    entry.value.author.contains(it)
                } ?: true
            }
            .filter { entry ->
                rq.event.takeIf { it.isNotBlank() }?.let {
                    entry.value.event.contains(it)
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbPostcardsResponseOk(result)
    }
}