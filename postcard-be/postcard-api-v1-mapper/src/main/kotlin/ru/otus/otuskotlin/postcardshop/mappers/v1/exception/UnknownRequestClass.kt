package ru.otus.otuskotlin.postcardshop.ru.otus.otuskotlin.postcardshop.mappers.v1.exception

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to PostcardContext")