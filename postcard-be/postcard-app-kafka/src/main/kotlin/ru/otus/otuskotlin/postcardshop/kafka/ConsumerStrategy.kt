package ru.otus.otuskotlin.postcardshop.kafka

import ru.otus.otuskotlin.postcardshop.common.PsContext

interface ConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: PsContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: PsContext)
}