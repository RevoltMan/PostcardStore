package ru.otus.otuskotlin.postcardshop.kafka

import ru.otus.otuskotlin.postcardshop.api.v1.models.IRequest
import ru.otus.otuskotlin.postcardshop.api.v1.models.Response
import ru.otus.otuskotlin.postcardshop.common.PsContext
import ru.otus.otuskotlin.postcardshop.mapper.apiV1RequestDeserialize
import ru.otus.otuskotlin.postcardshop.mapper.apiV1ResponseSerialize
import ru.otus.otuskotlin.postcardshop.mappers.v1.fromTransport
import ru.otus.otuskotlin.postcardshop.mappers.v1.toTransportPostcard

class ConsumerStrategyImpl : ConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicIn, config.kafkaTopicOut)
    }

    override fun serialize(source: PsContext): String {
        val response: Response = source.toTransportPostcard()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: PsContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}