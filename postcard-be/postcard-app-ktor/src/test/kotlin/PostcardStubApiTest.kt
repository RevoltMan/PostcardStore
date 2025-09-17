
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.api.v1.models.Request
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDebug
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDeleteResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardReadResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugMode.STUB
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugStubs.SUCCESS
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchFilter
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardSearchResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardUpdateResponse
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.ktor.PostcardAppSettingsImpl
import ru.otus.otuskotlin.postcardshop.ktor.module
import kotlin.test.assertEquals

class PostcardStubApiTest {
    private val successDebug = PostcardDebug(
        mode = STUB,
        stub = SUCCESS
    )
    @Test
    fun `success create`() = testApplication(
        func = "admin/create",
        request = PostcardCreateRequest(
            postcard = PostcardCreateObject(
                title = "Салют",
                author = setOf("Мальцева"),
                postcardEvent = setOf("Юбилей"),
                price = 100,
            ),
            debug = successDebug
        ),
    ) { response ->
        val responseObj = response.body<PostcardCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Бумбараш", responseObj.postcard?.id)
    }

    @Test
    fun `success read`() = testApplication(
        func = "postcard/read",
        request = PostcardReadRequest(
            postcard = PostcardReadObject("Бумбараш"),
            debug = successDebug
        ),
    ) { response ->
        val responseObj = response.body<PostcardReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Бумбараш", responseObj.postcard?.id)
    }

    @Test
    fun `success update`() = testApplication(
        func = "admin/update",
        request = PostcardUpdateRequest(
            postcard = PostcardUpdateObject(
                id = "Бумбараш",
                title = "Восход",
                author = setOf("Куприянов"),
                postcardEvent = setOf("Праздник"),
                price = 150,
            ),
            debug = successDebug
        ),
    ) { response ->
        val responseObj = response.body<PostcardUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Бумбараш", responseObj.postcard?.id)
        assertEquals("Синяя роза", responseObj.postcard?.title)
    }

    @Test
    fun `success delete`() = testApplication(
        func = "admin/delete",
        request = PostcardDeleteRequest(
            postcard = PostcardDeleteObject(
                id = "Колыбель",
            ),
            debug = successDebug
        ),
    ) { response ->
        val responseObj = response.body<PostcardDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Бумбараш", responseObj.postcard?.id)
    }

    @Test
    fun `success  search`() = testApplication(
        func = "postcard/search",
        request = PostcardSearchRequest(
            adFilter = PostcardSearchFilter(),
            debug = successDebug
        ),
    ) { response ->
        val responseObj = response.body<PostcardSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("Малевич", responseObj.postcards?.first()?.id)
    }

    private fun testApplication(
        func: String,
        request: Request,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(PostcardAppSettingsImpl(corSettings = PsCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}