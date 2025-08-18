
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.api.v1.models.IResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardResponseObject
import ru.otus.otuskotlin.postcardshop.mapper.apiV1Mapper

class ResponseV1SerializationTest {
    private val response = PostcardCreateResponse(
        postcard = PostcardResponseObject(
            title = "postcard title",
            author = setOf("Mari"),
            postcardEvent = setOf("event"),
            price = 10,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertTrue(json.contains(Regex("\"title\":\\s*\"postcard title\"")))
        assertTrue(json.contains(Regex("\"responseType\":\\s*\"create\"")))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as PostcardCreateResponse

        assertEquals(response, obj)
    }
}