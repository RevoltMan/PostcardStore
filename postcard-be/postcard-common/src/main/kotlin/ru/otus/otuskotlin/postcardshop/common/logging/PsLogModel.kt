package ru.otus.otuskotlin.postcardshop.common.logging

// Общая модель лога для всех микросервисов системы
data class CommonLogModel(
    val messageTime: String,
    val logId: String,
    val source: String,
    val postcard: PsLogModel?,
    val errors: List<ErrorLogModel>
)


enum class PsLogOperation{
    CREATE,
    READ,
    UPDATE,
    DELETE,
    SEARCH,
    INIT,
    FINISH,
    NONE,
}

// Модель лога для ошибки
data class ErrorLogModel (
    val message: String?,
    val field: String?,
    val code: String?,
    val level: String?,
)

// Модель лога для открытки
data class PostcardLog(
    val id: String?,
    val title: String,
    val author: Set<String>,
    val price: Int,
    val event: Set<String>,
    val ownerId: String,
    val permissions: Set<String> = mutableSetOf()
)

// Модель лога фильтра
data class PostcardFilterLog(
    val searchString: String?,
    val price: Int?,
    val author: String?,
    val ownerId: String?
)

// Модель лога для микросервиса Postcard
data class PsLogModel(
    val requestId: String? = null,
    val operation: PsLogOperation? = null,
    val requestPostcard: PostcardLog? = null,
    val requestFilter: PostcardFilterLog? = null,
    val responsePostcard: PostcardLog? = null,
    val responsePostcards: List<PostcardLog>? = null,
)

