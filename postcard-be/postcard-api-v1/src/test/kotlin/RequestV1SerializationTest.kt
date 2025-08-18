
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.api.v1.models.IRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDebug
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugMode
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugStubs
import ru.otus.otuskotlin.postcardshop.mapper.apiV1Mapper

class RequestV1SerializationTest {
    private val request = PostcardCreateRequest(
        debug = PostcardDebug(
            mode = PostcardRequestDebugMode.STUB,
            stub = PostcardRequestDebugStubs.BAD_TITLE
        ),
        postcard = PostcardCreateObject(
            title = "postcard title",
            author = setOf("Mari"),
            postcardEvent = setOf("event"),
            price = 10,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertTrue(json.contains(Regex("\"title\":\\s*\"postcard title\"")))
        assertTrue(json.contains(Regex("\"mode\":\\s*\"stub\"")))
        assertTrue(json.contains(Regex("\"stub\":\\s*\"badTitle\"")))
        assertTrue(json.contains(Regex("\"requestType\":\\s*\"create\"")))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as PostcardCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"postcard": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, PostcardCreateRequest::class.java)

        assertEquals(null, obj.postcard)
    }
}