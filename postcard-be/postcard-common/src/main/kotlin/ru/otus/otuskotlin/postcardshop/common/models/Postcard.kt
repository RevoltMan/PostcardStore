package ru.otus.otuskotlin.postcardshop.common.models

data class Postcard (
    var id: PostcardId = PostcardId.NONE,
    var title: String = "",
    var author: Set<String> = mutableSetOf(),
    var event: Set<String> = mutableSetOf(),
    var price: Int = 0,
    var ownerId: PsUserId = PsUserId.NONE,
    val permissionsClient: MutableSet<PostcardPermissionClient> = mutableSetOf()
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Postcard()
    }

}