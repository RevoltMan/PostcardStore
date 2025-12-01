package ru.otus.otuskotlin.postcardshop.repo.common

import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.repo.RepoPostcard

interface RepoPostcardInitializable: RepoPostcard {
    fun save(postcards: Collection<Postcard>) : Collection<Postcard>
}