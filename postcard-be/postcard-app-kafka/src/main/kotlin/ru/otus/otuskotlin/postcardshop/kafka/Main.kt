package ru.otus.otuskotlin.postcardshop.kafka

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyImpl()))
    consumer.start()
}