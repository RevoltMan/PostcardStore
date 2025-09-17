package ru.otus.otuskotlin.postcardshop.ktor

import ru.otus.otuskotlin.postcardshop.app.common.PostcardAppSettings
import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings

data class PostcardAppSettingsImpl(
    val appUrls: List<String> = emptyList(),
    override val corSettings: PsCorSettings = PsCorSettings(),
    override val processor: PsProcessor = PsProcessor(corSettings),
): PostcardAppSettings
