package ru.otus.otuskotlin.postcardshop.common.exceptions

import ru.otus.otuskotlin.postcardshop.common.models.PsCommand

class UnknownPostcardCommand(command: PsCommand) : Throwable("Wrong command $command at mapping toTransport stage")