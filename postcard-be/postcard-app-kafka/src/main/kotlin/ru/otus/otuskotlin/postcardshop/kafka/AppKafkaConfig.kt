package ru.otus.otuskotlin.postcardshop.kafka

import ru.otus.otuskotlin.postcardshop.app.common.PostcardAppSettings
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings
import ru.otus.otuskotlin.postcardshop.common.logging.PsLoggerProvider
import ru.otus.otuskotlin.postcardshop.common.logging.psLoggerLogback

class AppKafkaConfig (
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicIn: String = KAFKA_TOPIC_IN,
    val kafkaTopicOut: String = KAFKA_TOPIC_OUT,
    val ttl: Long = 3_600, // Время жизни сообщения по умолчанию
    override val corSettings: PsCorSettings = PsCorSettings(
        loggerProvider = PsLoggerProvider() { psLoggerLogback(it) }
    ),
    override val processor: PsProcessor = PsProcessor(corSettings),
): PostcardAppSettings {
    companion object {
        const val KAFKA_HOST_DEF = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_DEF = "KAFKA_TOPIC_IN"
        const val KAFKA_TOPIC_OUT_DEF = "KAFKA_TOPIC_OUT"
        const val KAFKA_GROUP_ID_DEF = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_DEF) ?: "").split("\\s*[,; ]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_DEF) ?: "postcard" }
        val KAFKA_TOPIC_IN by lazy { System.getenv(KAFKA_TOPIC_IN_DEF) ?: "postcard-in" }
        val KAFKA_TOPIC_OUT by lazy { System.getenv(KAFKA_TOPIC_OUT_DEF) ?: "postcard-out" }
    }
}