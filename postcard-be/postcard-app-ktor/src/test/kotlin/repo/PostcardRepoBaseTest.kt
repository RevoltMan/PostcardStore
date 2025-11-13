package ru.otus.otuskotlin.postcardshop.ktor.repo

import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDebug
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugMode
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchFilter
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.Request
import ru.otus.otuskotlin.postcardshop.app.common.PostcardAppSettings
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub
import ru.otus.otuskotlin.postcardshop.ktor.module
import ru.otus.otuskotlin.postcardshop.mapper.apiV1Mapper
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportCreatePostcard
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportDeletePostcard
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportReadPostcard
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportUpdatePostcard
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class PostcardRepoBaseTest {
    abstract val workMode: PostcardRequestDebugMode
    abstract val appSettingsCreate: PostcardAppSettings
    abstract val appSettingsRead: PostcardAppSettings
    abstract val appSettingsUpdate: PostcardAppSettings
    abstract val appSettingsDelete: PostcardAppSettings
    abstract val appSettingsSearch: PostcardAppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"

    protected val initPostcard = PostcardStub.prepareResult {
        id = PostcardId(uuidOld)
    }

    @Test
    fun create() {
        val postcard = initPostcard.toTransportCreatePostcard()
        v1TestApplication(
            conf = appSettingsCreate,
            func = "admin/create",
            request = PostcardCreateRequest(
                postcard = postcard,
                debug = PostcardDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<PostcardCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.postcard?.id)
            assertEquals(postcard.title, responseObj.postcard?.title)
            assertEquals(postcard.author, responseObj.postcard?.author)
            assertEquals(postcard.postcardEvent, responseObj.postcard?.postcardEvent)
            assertEquals(postcard.price, responseObj.postcard?.price)
        }
    }

    @Test
    fun read() {
        val postcard = initPostcard.toTransportReadPostcard()
        v1TestApplication(
            conf = appSettingsRead,
            func = "postcard/read",
            request = PostcardReadRequest(
                postcard = postcard,
                debug = PostcardDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<PostcardReadResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.postcard?.id)
        }
    }

    @Test
    fun update() {
        val postcard = initPostcard.toTransportUpdatePostcard()
        v1TestApplication(
            conf = appSettingsUpdate,
            func = "admin/update",
            request = PostcardUpdateRequest(
                postcard = postcard,
                debug = PostcardDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<PostcardUpdateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(postcard.id, responseObj.postcard?.id)
            assertEquals(postcard.title, responseObj.postcard?.title)
            assertEquals(postcard.author, responseObj.postcard?.author)
            assertEquals(postcard.postcardEvent, responseObj.postcard?.postcardEvent)
            assertEquals(postcard.price, responseObj.postcard?.price)
        }
    }
    @Test
    fun delete() {
        val postcard = initPostcard.toTransportDeletePostcard()
        v1TestApplication(
            conf = appSettingsDelete,
            func = "admin/delete",
            request = PostcardDeleteRequest(
                postcard = postcard,
                debug = PostcardDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<PostcardDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.postcard?.id)
        }
    }

    @Test
    fun search() = v1TestApplication(
        conf = appSettingsSearch,
        func = "postcard/search",
        request = PostcardSearchRequest(
            postcardFilter = PostcardSearchFilter(),
            debug = PostcardDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<PostcardSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.postcards?.size)
        assertEquals(uuidOld, responseObj.postcards?.first()?.id)
    }

    private inline fun <reified T: Request> v1TestApplication(
        conf: PostcardAppSettings,
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(appSettings = conf) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
        }
        val response = client.post("/v1/$func") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}
