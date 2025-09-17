package ru.otus.otuskotlin.postcardshop.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateObject
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardCreateResponse
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardDebug
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugMode
import ru.otus.otuskotlin.postcardshop.api.v1.models.PostcardRequestDebugStubs
import ru.otus.otuskotlin.postcardshop.mapper.apiV1RequestSerialize
import ru.otus.otuskotlin.postcardshop.mapper.apiV1ResponseDeserialize
import java.util.*

class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicIn
        val outputTopic = config.kafkaTopicOut

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyImpl()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        PostcardCreateRequest(
                            postcard = PostcardCreateObject(
                                title = "Попугай",
                                author = setOf("Колыбельная"),
                                postcardEvent = setOf("Вечер"),
                                price = 2500,
                            ),
                            debug = PostcardDebug(
                                mode = PostcardRequestDebugMode.STUB,
                                stub = PostcardRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<PostcardCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("Синяя роза", result.postcard?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}