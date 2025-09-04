package ru.otus.otuskotlin.postcardshop.common.stubs

import ru.otus.otuskotlin.postcardshop.common.models.Postcard
import ru.otus.otuskotlin.postcardshop.common.models.PostcardId
import ru.otus.otuskotlin.postcardshop.common.models.PostcardPermissionClient
import ru.otus.otuskotlin.postcardshop.common.models.PsUserId
import ru.otus.otuskotlin.postcardshop.common.stubs.PsStubRose.PS_ROSE1

object PsStubRose {
    val PS_ROSE1: Postcard
        get() = Postcard(
            id = PostcardId("Бумбараш"),
            title = "Синяя роза",
            author = setOf("Калинина Светлана"),
            event = setOf("8 Марта", "Праздник роз"),
            price = 1500,
            ownerId = PsUserId("User Main"),
            permissionsClient = mutableSetOf(
                PostcardPermissionClient.READ,
                PostcardPermissionClient.UPDATE,
                PostcardPermissionClient.DELETE,
                PostcardPermissionClient.MAKE_VISIBLE_PUBLIC,
                PostcardPermissionClient.MAKE_VISIBLE_GROUP,
                PostcardPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
    val PS_ROSE2 = PS_ROSE1.copy(title = "Алая роза")
}

object PostcardStub {
    fun get(): Postcard = PS_ROSE1.copy()

    fun prepareResult(block: Postcard.() -> Unit): Postcard = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        postcardDemand("Малевич", filter),
        postcardDemand("R-047-02", filter),
        postcardDemand("R-047-03", filter),
        postcardDemand("R-047-04", filter),
        postcardDemand("R-047-05", filter),
        postcardDemand("R-047-06", filter),
    )

    private fun postcardDemand(id: String, filter: String) =
        postcard(PS_ROSE1, id = id, filter = filter)

    private fun postcard(base: Postcard, id: String, filter: String) = base.copy(
        id = PostcardId(id),
        title = "$filter $id",
    )
}
