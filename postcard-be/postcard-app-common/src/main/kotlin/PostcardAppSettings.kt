package ru.otus.otuskotlin.postcardshop.app.common

import ru.otus.otuskotlin.postcardshop.biz.PsProcessor
import ru.otus.otuskotlin.postcardshop.common.PsCorSettings

interface PostcardAppSettings {
    val processor: PsProcessor
    val corSettings: PsCorSettings
}