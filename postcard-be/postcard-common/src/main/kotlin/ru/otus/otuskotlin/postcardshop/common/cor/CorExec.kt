package ru.otus.otuskotlin.postcardshop.common.cor

interface CorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}