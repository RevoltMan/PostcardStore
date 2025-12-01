package ru.otus.otuskotlin.postcardshop.ktor

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun root() = testApplication {
        application { module() }
        val response = client.get("/")
        assertEquals(response.status, HttpStatusCode.OK)
        assertEquals(response.bodyAsText(), "Hello, world!")
    }
}