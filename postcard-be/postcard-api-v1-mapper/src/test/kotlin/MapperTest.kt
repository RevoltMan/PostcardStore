import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDebug
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugMode
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugStubs
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PsCommand
import ru.otus.otuskotlin.postcardshop.common.models.PsError
import ru.otus.otuskotlin.postcardshop.common.models.PsRequestId
import ru.otus.otuskotlin.postcardshop.common.models.PsState
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId
import ru.otus.otuskotlin.postcardshop.common.models.PsWorkMode
import ru.otus.otuskotlin.postcardshop.common.stubs.PostcardStub
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubs
import ru.otus.otuskotlin.postcardshop.mappers.v1.fromTransport
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportAd
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportCreatePostcard
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportPostcard

class MapperTest {

    @Test
    fun fromTransport() {
        val req = PostcardCreateRequest(
            debug = PostcardDebug(
                mode = PostcardRequestDebugMode.STUB,
                stub = PostcardRequestDebugStubs.SUCCESS,
            ),
            postcard = PostcardStub.get().toTransportCreatePostcard()
        )
        val expected = PostcardStub.prepareResult {
            id = PostcardId.NONE
            ownerId = PsUserId.NONE
            permissionsClient.clear()
        }

        val context = PsContext()
        context.fromTransport(req)

        assertEquals(PsStubs.SUCCESS, context.stubCase)
        assertEquals(PsWorkMode.STUB, context.workMode)
        assertEquals(expected, context.postcardRequest)
    }

    @Test
    fun toTransport() {
        val context = PsContext(
            requestId = PsRequestId("1234"),
            command = PsCommand.CREATE,
            postcardResponse = PostcardStub.get(),
            errors = mutableListOf(
                PsError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = PsState.RUNNING,
        )

        val req = context.toTransportAd() as PostcardCreateResponse

        assertEquals(req.postcard, PostcardStub.get().toTransportPostcard())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}