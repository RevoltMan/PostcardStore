package ru.otus.otuskotlin.postcardshop.mapper

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import ru.otus.otuskotlin.postcardshop.api.v1.models.Request
import ru.otus.otuskotlin.postcardshop.api.v1.models.Response

val apiV1Mapper = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
    build()
}

@Suppress("unused")
fun apiV1RequestSerialize(request: Request): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : Request> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, Request::class.java) as T

@Suppress("unused")
fun apiV1ResponseSerialize(response: Response): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : Response> apiV1ResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, Response::class.java) as T